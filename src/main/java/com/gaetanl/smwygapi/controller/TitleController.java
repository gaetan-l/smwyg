package com.gaetanl.smwygapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gaetanl.smwygapi.util.ApiUtil;
import com.gaetanl.smwygapi.model.Genre;
import com.gaetanl.smwygapi.repository.TitleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TitleController {
    private final String TMDB_API_KEY = "91b96d8d3dc851fb01aa9a36e8c81880";
    private final String TMDB_URI_ROOT = "https://api.themoviedb.org/3";
    private final String TMDB_URI_TITLES_DEFAULT = "/movie/popular";
    private final String TMDB_URI_GENRES = "/genre/movie/list";

    @Autowired
    private TitleRepository titleRepository;

    private static final Logger logger = LoggerFactory.getLogger(TitleController.class);

    /**
     * Reads titles using the "default" function of the third-party API,
     * it can be the latest titles, or those with the best score, whatever is,
     * available. The goal is to have a "default" list of titles to show when
     * no criteria are selected by the user.
     *
     * @return a list of titles
     */
    @CrossOrigin(origins = "http://localhost")
    @GetMapping("/titles")
    public ResponseEntity<String> readTitles() {
        StringBuilder sb = new StringBuilder(100);
        sb.append(this.TMDB_URI_ROOT);
        sb.append(this.TMDB_URI_TITLES_DEFAULT);
        sb.append("?api_key=");
        sb.append(this.TMDB_API_KEY);

        HttpHeaders responseHeaders = new HttpHeaders();
        String body = "{}";
        try {
            WebClient client = WebClient.create();
            URI uri = new URI(sb.toString());
            logger.info(uri.toString());

            body = client.get()
                    .uri(uri)
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

    /**
     * Reads the available genres used by the API.
     *
     * @return the list of genres used by the API
     */
    @CrossOrigin(origins = "http://localhost")
    @GetMapping("/genres")
    public ResponseEntity<String> readMovieGenres() {
        HttpHeaders responseHeaders = new HttpHeaders();
        String body = "[]";

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> genres = new HashMap<>();

        try {
            /*
             * This list is not supposed to change, so the API call will only be
             * done once and populate the local map.
             */
            if (Genre.list.isEmpty()) {
                StringBuilder sb = new StringBuilder(100);
                sb.append(this.TMDB_URI_ROOT);
                sb.append(this.TMDB_URI_GENRES);
                sb.append("?api_key=");
                sb.append(this.TMDB_API_KEY);

                WebClient client = WebClient.create();
                URI uri = new URI(sb.toString());
                logger.info(uri.toString());

                body = client.get()
                        .uri(uri)
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();

                JsonNode jsonGenresList = objectMapper.readTree(body).get("genres");
                ObjectReader objectReader = objectMapper.readerFor(new TypeReference<ArrayList<Genre>>() {});

                /*
                 * The simple fact of using Jackson to deserialize the result
                 * will trigger the populating of Genre.list because at every
                 * call of it Genre constructor the new Genre is added to the
                 * list.
                 */
                objectReader.readValue(jsonGenresList);
            }

            /*
             * Manual deserializing of a Map intro an array,
             * Jackson translates Map to a JSON object by default.
             */
            ArrayNode rootNode = objectMapper.createArrayNode();
            for (Map.Entry<Integer, String> entry: Genre.list.entrySet()) {
                ObjectNode childNode = objectMapper.createObjectNode();
                childNode.put("id", entry.getKey());
                childNode.put("name", entry.getValue());
                rootNode.add(childNode);
            }
            body = objectMapper.writeValueAsString(rootNode);
        }
        catch (IOException | URISyntaxException e) {
            ApiUtil.putExceptionInResponseHeaders(responseHeaders, e);

            /*
             * If an error occurs, return an empty list.
             */
            return new ResponseEntity<String>(
                    "[]",
                    responseHeaders,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(
                body,
                responseHeaders,
                HttpStatus.OK);
    }
}
