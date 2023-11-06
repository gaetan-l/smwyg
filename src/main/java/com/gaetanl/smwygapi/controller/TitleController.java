package com.gaetanl.smwygapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaetanl.smwygapi.ApiUtil;
import com.gaetanl.smwygapi.repository.TitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class TitleController {
    private final String IMDB8_API_KEY = "d90ee7ff9emsh4661d06e21dac39p15fbaejsned54758f9253";
    private final String IMDB8_API_HOST = "imdb8.p.rapidapi.com";
    private final String IMDB8_API_ROOT_URI = "https://imdb8.p.rapidapi.com";
    private final String IMDB8_URI_DEFAULT_TITLES = "/title/get-most-popular-movies?currentCountry=FR";
    private final String IMDB8_URI_TITLE_DETAILS ="/title/get-details";
    private final String IMDB8_URI_TITLE_KEYWORDS ="/title/get-genres";

    private final String TMDB_API_KEY = "91b96d8d3dc851fb01aa9a36e8c81880";
    private final String TMDB_API_ROOT_URI = "https://api.themoviedb.org/3/";
    private final String IMAGE_API_ROOT_URI = "https://image.tmdb.org/t/p/w1280";
    private final String TMDB_URI_DEFAULT_TITLES = "movie/popular";
    private final String TMDB_URI_MOVIE_GENRES = "genre/movie/list";

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
    @CrossOrigin(origins = "http://localhost")
    @GetMapping("/title")
    public ResponseEntity<String> readTitles() {
        HttpHeaders responseHeaders = new HttpHeaders();

        ObjectMapper objectMapper = new ObjectMapper();
        String body = "[]";
        try {
            WebClient client = WebClient.create();

            Mono<String[]> response = client.get()
                    .uri(new URI(this.IMDB8_API_ROOT_URI + this.IMDB8_URI_DEFAULT_TITLES))
                    .header("X-RapidAPI-Key", this.IMDB8_API_KEY)
                    .header("X-RapidAPI-Host", this.IMDB8_API_HOST)
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

    /**
     * Reads the details of a title using its id
     *
     * @return the details of the title
     */
    @CrossOrigin(origins = "http://localhost")
    @GetMapping("/title-details/{id}")
    public ResponseEntity<String> readTitleDetails(@PathVariable("id") String id) {
        HttpHeaders responseHeaders = new HttpHeaders();

        ObjectMapper objectMapper = new ObjectMapper();
        String body = "{}";

        try {
            WebClient client = WebClient.create();

            body = client.get()
                    .uri(new URI(this.IMDB8_API_ROOT_URI + this.IMDB8_URI_TITLE_DETAILS + "?tconst=" + id))
                    .header("X-RapidAPI-Key", this.IMDB8_API_KEY)
                    .header("X-RapidAPI-Host", this.IMDB8_API_HOST)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        }
        catch (WebClientResponseException we) {
            ApiUtil.putExceptionInResponseHeaders(responseHeaders, we);

            return new ResponseEntity<String>(
                    body,
                    responseHeaders,
                    HttpStatus.NOT_FOUND);
        }
        catch (URISyntaxException e) {
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

    /**
     * Reads the keywords of a title using its id
     *
     * @return the keywords of the title
     */
    @CrossOrigin(origins = "http://localhost")
    @GetMapping("/title-keywords/{id}")
    public ResponseEntity<String> readTitleKeywords(@PathVariable("id") String id) {
        HttpHeaders responseHeaders = new HttpHeaders();

        ObjectMapper objectMapper = new ObjectMapper();
        String body = "[]";

        try {
            WebClient client = WebClient.create();

            Mono<String[]> response = client.get()
                    .uri(new URI(this.IMDB8_API_ROOT_URI + this.IMDB8_URI_TITLE_KEYWORDS + "?tconst=" + id))
                    .header("X-RapidAPI-Key", this.IMDB8_API_KEY)
                    .header("X-RapidAPI-Host", this.IMDB8_API_HOST)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String[].class);

            String[] responseArray = response.block();
            body = objectMapper.writeValueAsString(responseArray);
        }
        catch (WebClientResponseException we) {
            return new ResponseEntity<String>(
                    body,
                    responseHeaders,
                    HttpStatus.NOT_FOUND);
        }
        catch (JsonProcessingException | URISyntaxException e) {
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

    @CrossOrigin(origins = "http://localhost")
    @GetMapping("/popular-titles")
    public ResponseEntity<String> readPopularTitles() {
        HttpHeaders responseHeaders = new HttpHeaders();

        ObjectMapper objectMapper = new ObjectMapper();
        String body = "[]";
        try {
            WebClient client = WebClient.create();

            body = client.get()
                    .uri(new URI(this.TMDB_API_ROOT_URI + this.TMDB_URI_DEFAULT_TITLES + "?api_key=" + this.TMDB_API_KEY + "&page=1"))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        }
        catch (URISyntaxException e) {
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

    @CrossOrigin(origins = "http://localhost")
    @GetMapping("/movie-genres")
    public ResponseEntity<String> readMovieGenres() {
        HttpHeaders responseHeaders = new HttpHeaders();

        ObjectMapper objectMapper = new ObjectMapper();
        String body = "{}";
        try {
            WebClient client = WebClient.create();

            body = client.get()
                    .uri(new URI(this.TMDB_API_ROOT_URI + this.TMDB_URI_MOVIE_GENRES + "?api_key=" + this.TMDB_API_KEY))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        }
        catch (URISyntaxException e) {
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
