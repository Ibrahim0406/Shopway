package com.shopway.shopway.services;

import com.shopway.shopway.auth.dto.OrderResponse;
import com.shopway.shopway.auth.entities.User;
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
}
