package com.gaetanl.smwygapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaetanl.smwygapi.ApiUtil;
import com.gaetanl.smwygapi.repository.TitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

@RestController
public class TitleController {
    private final String API_KEY = "d90ee7ff9emsh4661d06e21dac39p15fbaejsned54758f9253";
    private final String API_HOST = "imdb8.p.rapidapi.com";
    private final String API_ROOT_URI = "https://imdb8.p.rapidapi.com";
    private final String URI_DEFAULT_TITLES = "/title/get-most-popular-movies?currentCountry=FR";

    @Autowired
    private TitleRepository titleRepository;

    /**
     * Reads titles using the "default" function of the third-party API,
     * it can be the latest titles, or those with the best score, whatever,
     * the goal is to have a "default" list of titles to show when no criteria
     * are selected by the user.
     *
     * @return a list of title ids
     */
    @GetMapping("/title")
    public ResponseEntity<String> readTitles() {
        HttpHeaders responseHeaders = new HttpHeaders();

        ObjectMapper objectMapper = new ObjectMapper();
        String body = "[]";
        try {
            WebClient client = WebClient.create();

            Mono<String[]> response = client.get()
                    .uri(new URI(this.API_ROOT_URI + this.URI_DEFAULT_TITLES))
                    .header("X-RapidAPI-Key", this.API_KEY)
                    .header("X-RapidAPI-Host", this.API_HOST)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String[].class);

            String[] responseArray = response.block();

            body = objectMapper.writeValueAsString(responseArray);
        }
        catch (URISyntaxException | JsonProcessingException e) {
            ApiUtil.putExceptionInResponseHeaders(responseHeaders, e);

            return new ResponseEntity<String>(
                    body,
                    responseHeaders,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(
                body,
                responseHeaders,
                HttpStatus.OK);
    }
}
