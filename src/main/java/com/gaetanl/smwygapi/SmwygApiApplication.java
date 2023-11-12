package com.gaetanl.smwygapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SuppressWarnings({"NonFinalUtilityClass", "WeakerAccess"}) // Must be that way for Spring to run the app
public class SmwygApiApplication {
	public static void main(final String[] args) {
		SpringApplication.run(SmwygApiApplication.class, args);
	}
}
