package com.gaetanl.smwygapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaetanl.smwygapi.model.Title;
import com.gaetanl.smwygapi.service.TitleService;
import com.gaetanl.smwygapi.util.ApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @GetMapping("/titles")
    public ResponseEntity<String> readTitles(@RequestParam(required = false) Integer page) {
        String body = "[]";
        HttpHeaders responseHeaders = new HttpHeaders();

        try {
            List<Title> titles = titleService.getTitles(page);

            ObjectMapper objectMapper = new ObjectMapper();
            body = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(titles);
        }
        catch (WebClientResponseException | URISyntaxException e) {
            ApiUtil.putExceptionInResponseHeaders(responseHeaders, e);

            return new ResponseEntity<>(
                    body,
                    responseHeaders,
                    HttpStatus.NOT_FOUND);
        }
        catch (IOException e) {
            ApiUtil.putExceptionInResponseHeaders(responseHeaders, e);

            return new ResponseEntity<>(
                    body,
                    responseHeaders,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(
                body,
                responseHeaders,
                HttpStatus.OK);
    }
}
