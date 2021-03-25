package com.croquiscom.vincentj2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class Vincentj2Application {

	public static void main(String[] args) {
		SpringApplication.run(Vincentj2Application.class, args);
	}

}
