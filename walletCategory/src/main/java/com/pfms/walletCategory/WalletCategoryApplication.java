package com.pfms.walletCategory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@SpringBootApplication
@EnableEurekaClient
public class WalletCategoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletCategoryApplication.class, args);
	}

}
