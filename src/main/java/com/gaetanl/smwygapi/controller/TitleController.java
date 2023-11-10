package com.gaetanl.smwygapi.controller;

import com.gaetanl.smwygapi.model.Title;
import com.gaetanl.smwygapi.service.TitleService;
import com.gaetanl.smwygapi.util.ApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

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
}
