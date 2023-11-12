package com.gaetanl.smwygapi.controller;

import com.gaetanl.smwygapi.model.Title;
import com.gaetanl.smwygapi.service.TitleService;
import com.gaetanl.smwygapi.util.ApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.*;
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
    public @NonNull ResponseEntity<String> readAll(@RequestParam(required = false) final String order, @RequestParam(required = false) final Integer page) {
        final HttpHeaders responseHeaders = new HttpHeaders();
        final List<Title> titles;

        String body = "[]";
        HttpStatus httpStatus = OK;
        Exception exception = null;

        try {
            final Title.TitleIndex index = order == null ? null : Title.TitleIndex.valueOf(order.toUpperCase()); // throws IllegalArgumentException, NullPointerException
            titles = titleService.readAll(index, page);
            body = ApiUtil.getObjectAsPrettyJson(titles, "[]", responseHeaders);
        }
        catch (final IllegalArgumentException | NullPointerException e) {
            exception = e;
            httpStatus = BAD_REQUEST;
        }
        catch (final WebClientResponseException | URISyntaxException e) {
            exception = e;
            httpStatus = NOT_FOUND;
        }
        catch (final IOException e) {
            exception = e;
            httpStatus = INTERNAL_SERVER_ERROR;
        }
        finally {
            if (exception != null) ApiUtil.putExceptionInResponseHeaders(responseHeaders, exception);
        }

        return new ResponseEntity<>(
                body,
                responseHeaders,
                httpStatus);
    }

    /**
     * Reads a single title.
     */
    @CrossOrigin(origins = "http://localhost")
    @GetMapping("/title/{id}")
    public @NonNull ResponseEntity<String> read(@PathVariable("id") @NonNull final String id) {
        final HttpHeaders responseHeaders = new HttpHeaders();
        final Optional<Title> foundTitle;

        String body = "{}";
        HttpStatus httpStatus = OK;
        Exception exception = null;

        try {
            foundTitle = titleService.read(id);
            if (foundTitle.isEmpty()) throw new IllegalArgumentException(String.format("{Title with id=%s} not found", id));
            body = ApiUtil.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(foundTitle.get());
        }
        catch (final WebClientResponseException | URISyntaxException | IllegalArgumentException e) {
            exception = e;
            httpStatus = NOT_FOUND;
        }
        catch (final IOException e) {
            exception = e;
            httpStatus = INTERNAL_SERVER_ERROR;
        }
        finally {
            if (exception != null) ApiUtil.putExceptionInResponseHeaders(responseHeaders, exception);
        }

        return new ResponseEntity<>(
                body,
                responseHeaders,
                httpStatus);
    }
}
