package com.shopway.shopway.services;

import com.shopway.shopway.auth.dto.OrderResponse;
import com.shopway.shopway.auth.entities.User;
import com.shopway.shopway.dto.OrderDetails;
import com.shopway.shopway.dto.OrderItemDetails;
import com.shopway.shopway.dto.OrderRequest;
import com.shopway.shopway.entities.*;
import com.shopway.shopway.repositories.OrderRepository;
import com.stripe.model.PaymentIntent;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;

/*
 * Servis za upravljanje narudžbinama i plaćanjima.
 */
@Service
public class OrderService {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ProductService productService;
    @Autowired
    private PaymentIntentService paymentIntentService;

    /*
     * Kreira novu narudžbinu za korisnika.
     *
     * Proces:
     * 1. Pronalazi korisnika preko Principal-a
     * 2. Validira da li adresa pripada korisniku
     * 3. Kreira Order entitet sa statusom PENDING
     * 4. Za svaki proizvod u narudžbini kreira OrderItem
     * 5. Kreira Payment objekat sa statusom PENDING
     * 6. Ako je način plaćanja kartica, kreira Stripe PaymentIntent
     *
     * @param orderRequest DTO sa podacima narudžbine
     * @param principal objekat sa informacijama o autentifikovanom korisniku
     * @return OrderResponse sa ID-em narudžbine i Stripe credentials-ima (ako je kartica)
     * @throws Exception ako adresa ne pripada korisniku ili proizvod ne postoji
     */
    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest, Principal principal) throws Exception {
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        Address address = user.getAddressesList().stream()
                .filter((address1) -> orderRequest.getAddressId().equals(address1.getId()))
                .findFirst().orElseThrow(BadRequestException::new);

        Order order = Order.builder()
                .user(user)
                .address(address)
                .totalAmount(orderRequest.getTotalAmount())
                .orderDate(orderRequest.getOrderDate())
                .discount(orderRequest.getDiscount())
                .excpectedDeliveryDate(orderRequest.getExceptedDeliveryDate())
                .paymentMethod(orderRequest.getPaymentMethod())
                .orderStatus(OrderStatus.PENDING)

                .build();

        List<OrderItem> orderItems = orderRequest.getOrderItemRequests().stream().map(orderItemRequest -> {
            try {
                Product product = productService.fetchProductById(orderItemRequest.getProductId());
//                ProductVariant productVariant = product.getProductVariants().stream().filter(productVariant1 -> productVariant1.getId() == orderItemRequest.getProductVariantId()).findFirst().orElseThrow(BadRequestException::new);
                OrderItem orderItem = OrderItem.builder()
                        .product(product)
                        .productVariantId(orderItemRequest.getProductVariantId())
                        .quantity(orderItemRequest.getQuantity())
                        .order(order)
                        .build();
                return orderItem;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            }).toList();

        order.setOrderItemList(orderItems);
        Payment payment = new Payment();
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setPaymentDate(new Date());
        payment.setOrder(order);
        payment.setAmount(orderRequest.getTotalAmount());
        payment.setPaymentMethod(order.getPaymentMethod());
        order.setPayment(payment);
        Order savedOrder = orderRepository.save(order);

        OrderResponse orderResponse = OrderResponse.builder()
                .paymentMethod(orderRequest.getPaymentMethod())
                .orderId(savedOrder.getId())
                .build();
        if(Objects.equals(orderRequest.getPaymentMethod(), "CARD")){
            orderResponse.setCredentials(paymentIntentService.createPaymentIntent(order));
        }

        return orderResponse;
    }
    /*
     * Ažurira status plaćanja nakon što Stripe potvrdi uspešno plaćanje.
     *
     * Proces:
     * 1. Dohvata PaymentIntent iz Stripe-a
     * 2. Proverava da li je plaćanje uspešno (status "succeeded")
     * 3. Pronalazi narudžbinu preko orderId iz metadata
     * 4. Ažurira Payment status na COMPLETED
     * 5. Postavlja paymentMethod iz Stripe response-a
     *
     * @param paymentIntentId Stripe PaymentIntent ID
     * @param status status plaćanja (trenutno se ne koristi, proverava se direkt iz Stripe-a)
     * @return mapa sa orderId ključem
     * @throws IllegalArgumentException ako plaćanje nije pronađeno ili nije uspešno
     */
    public Map<String, String> updateStatus(String paymentIntentId, String status) {

        try{
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            if (paymentIntent != null && paymentIntent.getStatus().equals("succeeded")) {
                    String orderId = paymentIntent.getMetadata().get("orderId");
                    Order order = orderRepository.findById(UUID.fromString(orderId)).orElseThrow(BadRequestException::new);
                    Payment payment = order.getPayment();
                    payment.setPaymentStatus(PaymentStatus.COMPLETED);
                    payment.setPaymentMethod(paymentIntent.getPaymentMethod());
                    order.setPaymentMethod(paymentIntent.getPaymentMethod());
                    order.setPayment(payment);
                    Order savedOrder = orderRepository.save(order);
                Map<String, String> map = new HashMap<>();
                map.put("orderId", savedOrder.getId().toString());
                return map;
            }else{
                throw new IllegalArgumentException("Payment not found or not succeeded");
            }

        } catch (Exception e) {
            throw new IllegalArgumentException("Payment not found or not succeeded");
        }
    }

    /*
     * Vraća sve narudžbine korisnika.
     * Mapira Order entitete u OrderDetails DTO objekte sa svim detaljima.
     *
     * @param name email korisnika
     * @return lista OrderDetails sa svim narudžbinama korisnika
     */
    public List<OrderDetails> getOrdersByUser(String name) {
        User user = (User) userDetailsService.loadUserByUsername(name);
        List<Order> orders = orderRepository.findByUser(user);
        return orders.stream().map(order -> {
            return OrderDetails.builder()
                    .id(order.getId())
                    .orderDate(order.getOrderDate())
                    .orderStatus(order.getOrderStatus())
                    .shipmentNumber(order.getShipmentTrackingNumber())
                    .address(order.getAddress())
                    .totalAmount(order.getTotalAmount())
                    .orderItemList(getItemDetails(order.getOrderItemList()))
                    .expectedDeliveryDate(order.getExcpectedDeliveryDate())
                    .build();
        }).toList();
    }

    /*
     * Pomoćna metoda koja mapira OrderItem entitete u OrderItemDetails DTO objekte.
     *
     * @param orderItemList lista OrderItem entiteta
     * @return lista OrderItemDetails DTO objekata
     */
    private List<OrderItemDetails> getItemDetails(List<OrderItem> orderItemList) {
        return orderItemList.stream().map(orderItem -> {
            return OrderItemDetails.builder()
                    .id(orderItem.getId())
                    .itemPrice(orderItem.getItemPrice())
                    .product(orderItem.getProduct())
                    .productVariantId(orderItem.getProductVariantId())
                    .quantity(orderItem.getQuantity())
                    .build();
        }).toList();

    }
    /*
     * Otkazuje narudžbinu.
     * Proverava da li narudžbina pripada korisniku pre otkazivanja.
     * Postavlja status na CANCELLED.
     *
     * TODO: Implementirati logiku za refundiranje iznosa ako je plaćanje već izvršeno.
     *
     * @param id UUID narudžbine
     * @param principal objekat sa informacijama o autentifikovanom korisniku
     * @throws RuntimeException ako narudžbina ne pripada korisniku
     */
    public void cancelOrder(UUID id, Principal principal) {
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        Order order = orderRepository.findById(id).get();
        if(null != order && order.getUser().getId().equals(user.getId())){
            order.setOrderStatus(OrderStatus.CANCELLED);
            //logic to refund amount
            orderRepository.save(order);
        }
        else{
            new RuntimeException("Invalid request");
        }

    }

}
