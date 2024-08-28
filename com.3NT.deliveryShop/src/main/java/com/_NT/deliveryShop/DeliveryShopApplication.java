package com._NT.deliveryShop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class DeliveryShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryShopApplication.class, args);
	}

}
