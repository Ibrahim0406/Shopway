package com.shopway.shopway.controllers;


import com.shopway.shopway.auth.dto.OrderResponse;
import com.shopway.shopway.dto.OrderDetails;
import com.shopway.shopway.dto.OrderRequest;
import com.shopway.shopway.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/*
 * REST kontroler za upravljanje narudžbinama.
 */
@RestController
@RequestMapping("/api/order")
@CrossOrigin
public class OrderController {

    @Autowired
    OrderService orderService;

    /*
     * Kreira novu narudžbinu za trenutno prijavljenog korisnika.
     * Ako je metoda plaćanja kartica, automatski kreira Stripe PaymentIntent.
     *
     * @param orderRequest DTO sa podacima narudžbine (proizvodi, adresa, iznos, način plaćanja)
     * @param principal objekat sa informacijama o autentifikovanom korisniku
     * @return ResponseEntity sa OrderResponse objektom koji sadrži:
     *         - orderId: UUID kreirane narudžbine
     *         - credentials: Stripe client_secret za kartično plaćanje (ako je metoda CARD)
     *         - paymentMethod: način plaćanja
     * @throws Exception ako dođe do greške prilikom kreiranje narudžbine ili plaćanja
     */
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest, Principal principal) throws Exception {
            OrderResponse orderResponse = orderService.createOrder(orderRequest, principal);
            return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    /*
     * Ažurira status plaćanja narudžbine nakon što Stripe potvrdi plaćanje.
     * Poziva se sa frontend-a nakon uspešnog plaćanja.
     *
     * @param request mapa sa "paymentIntent" (Stripe ID) i "status" ključevima
     * @return ResponseEntity sa HTTP statusom 200 OK
     */
    @PostMapping("/update-payment")
    public ResponseEntity<?> updatePaymentStatus(@RequestBody Map<String, String> request){
       Map<String, String> response = orderService.updateStatus(request.get("paymentIntent"), request.get("status"));
        return new ResponseEntity<>(HttpStatus.OK);
    }
    /*
     * Otkazuje narudžbinu.
     * Napomena: U kodu postoji greška - koristi se @PathVariable orderId ali u metodi se traži parametar id.
     *
     * @param id UUID narudžbine koja se otkazuje
     * @param principal objekat sa informacijama o autentifikovanom korisniku
     * @return ResponseEntity sa HTTP statusom 200 OK
     */
    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable UUID id, Principal principal){
        orderService.cancelOrder(id, principal);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
     * Vraća sve narudžbine trenutno prijavljenog korisnika.
     *
     * @param principal objekat sa informacijama o autentifikovanom korisniku
     * @return ResponseEntity sa listom OrderDetails i HTTP statusom 200 OK
     */
    @GetMapping("/user")
    public ResponseEntity<List<OrderDetails>> getOrderByUser(Principal principal){
        List<OrderDetails> orders = orderService.getOrdersByUser(principal.getName());

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
