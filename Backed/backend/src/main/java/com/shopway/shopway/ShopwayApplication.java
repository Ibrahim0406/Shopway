package com.shopway.shopway;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
public class ShopwayApplication {

	@Value("${stripe.secret}")
	private String stripeSecret;


	public static void main(String[] args) {
		SpringApplication.run(ShopwayApplication.class, args);
	}

	@PostConstruct
	public void init(){
		if (stripeSecret == null || stripeSecret.isEmpty()) {
			System.err.println("❌ STRIPE_SECRET_KEY nije učitan!");
		} else {
			System.out.println("✅ Stripe API Key loaded: " + stripeSecret.substring(0, 7) + "...");
			Stripe.apiKey = this.stripeSecret;
		}
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOriginPatterns(Collections.singletonList("*")); // Insecure, but for demo purposes it's ok
		config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "responseType", "Authorization", "x-authorization", "content-range","range"));
		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
		config.setExposedHeaders(Arrays.asList("X-Total-Count", "X-Total-Count", "content-range", "Content-Type", "Accept", "X-Requested-With", "remember-me"));
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter();
	}
}
