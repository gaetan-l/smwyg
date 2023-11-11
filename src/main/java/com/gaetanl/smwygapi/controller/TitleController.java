package com.gaetanl.smwygapi.controller;

import com.gaetanl.smwygapi.model.Title;
import com.gaetanl.smwygapi.service.TitleService;
import com.gaetanl.smwygapi.util.ApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
public class TitleController {
    @Autowired
    private
    TitleService titleService;

    /**
     * Reads titles using the "default" function of the third-party API,
     * it can be the latest titles, or those with the best score, whatever is,
     * available. The goal is to have a "default" list of titles to show when
     * no criteria are selected by the user.
     *
     * @return a list of titles
     */
    @CrossOrigin(origins = "http://localhost")
    @GetMapping("/title")
    public @NonNull ResponseEntity<String> readAll(@RequestParam(required = false) final Integer page) {
        final HttpHeaders responseHeaders = new HttpHeaders();
        final List<Title> titles;

        try {
            titles = titleService.readAll(page);
        }
        catch (final WebClientResponseException | URISyntaxException e) {
            ApiUtil.putExceptionInResponseHeaders(responseHeaders, e);

            return new ResponseEntity<>(
                    "[]",
                    responseHeaders,
                    HttpStatus.NOT_FOUND);
        }
        catch (final IOException e) {
            ApiUtil.putExceptionInResponseHeaders(responseHeaders, e);

            return new ResponseEntity<>(
                    "[]",
                    responseHeaders,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(
                ApiUtil.getObjectAsPrettyJson(titles, "[]", responseHeaders),
                responseHeaders,
                HttpStatus.OK);
    }

    /**
     * Reads a single title.
     */
    @CrossOrigin(origins = "http://localhost")
    @GetMapping("/title/{id}")
    public @NonNull ResponseEntity<String> read(@PathVariable("id") @NonNull final String id) {
        final HttpHeaders responseHeaders = new HttpHeaders();
        final Optional<Title> foundTitle;
        final String jsonTitle;

        try {
            foundTitle = titleService.read(id);
            if (foundTitle.isEmpty()) throw new IllegalArgumentException(String.format("{Title with id=%s} not found", id));
            jsonTitle = ApiUtil.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(foundTitle.get());
        }
        catch (final WebClientResponseException | URISyntaxException | IllegalArgumentException e) {
            ApiUtil.putExceptionInResponseHeaders(responseHeaders, e);

            return new ResponseEntity<>(
                    "{}",
                    responseHeaders,
                    HttpStatus.NOT_FOUND);
        }
        catch (final IOException e) {
            ApiUtil.putExceptionInResponseHeaders(responseHeaders, e);

            return new ResponseEntity<>(
                    "{}",
                    responseHeaders,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(
                jsonTitle,
                responseHeaders,
                HttpStatus.OK);
    }
}
