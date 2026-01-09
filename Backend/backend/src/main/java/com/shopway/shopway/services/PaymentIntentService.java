package com.shopway.shopway.services;

import com.shopway.shopway.auth.entities.User;
import com.shopway.shopway.entities.Order;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/*
 * Servis za kreiranje Stripe PaymentIntent objekata za kartična plaćanja.
 */
@Component
public class PaymentIntentService {

    /*
     * Kreira Stripe PaymentIntent za narudžbinu.
     *
     * PaymentIntent predstavlja nameru za plaćanje i sadrži:
     * - customer: ID korisnika (koristi se UUID korisnika iz baze)
     * - amount: iznos u najmanjoj valutnoj jedinici (NAPOMENA: hardkodovano 10, trebalo bi koristiti order.getTotalAmount())
     * - currency: valuta plaćanja (BAM - Konvertibilna marka)
     * - metadata: dodatni podaci - orderId za povezivanje plaćanja sa narudžbinom
     * - automaticPaymentMethods: omogućava automatsku detekciju dostupnih načina plaćanja
     *
     * @param order Order objekat za koji se kreira plaćanje
     * @return mapa sa "client_secret" ključem koji se koristi na frontendu za Stripe checkout
     * @throws StripeException ako dođe do greške u komunikaciji sa Stripe API-jem
     */
    public Map<String, String> createPaymentIntent(Order order) throws StripeException {
       User user = order.getUser();
       Map<String, String> metaData = new HashMap<>();
       metaData.put("orderId", order.getId().toString());
        PaymentIntentCreateParams paymentIntentCreateParams = PaymentIntentCreateParams.builder()
               .setCustomer(user.getId().toString())
                .setAmount(10L)
                .setCurrency("BAM")
                .putAllMetadata(metaData)
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build()
                )
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(paymentIntentCreateParams);
        Map<String, String> map = new HashMap<>();
        map.put ("client_secret", paymentIntent.getClientSecret());
        return map;
    }
}
