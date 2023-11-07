package com.gaetanl.smwygapi;

import com.gaetanl.smwygapi.controller.TitleController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
public class SmwygApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(SmwygApiApplication.class, args);
	}
}
