package com.gaetanl.smwygapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gaetanl.smwygapi.model.User;
import com.gaetanl.smwygapi.service.UserService;
import com.gaetanl.smwygapi.util.ApiUtil;
import com.gaetanl.smwygapi.util.MalformedJsonParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import static org.springframework.http.HttpStatus.*;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    // TODO: add endpoint logIn()?
    // TODO: replace with signUp()?
    @PutMapping("/user")
    public @NonNull ResponseEntity<String> create(@RequestBody final String jsonUser) {
        final HttpHeaders responseHeaders = new HttpHeaders();
        final User newUser, savedUser;

        String body = "{}";
        HttpStatus httpStatus = CREATED;
        Exception exception = null;

        try {
            newUser = ApiUtil.getObjectMapper().readValue(jsonUser, User.class);
            if (newUser.getId() != null) throw new MalformedJsonParameter("Cannot specify an id in body when creating a user");
            savedUser = userService.create(newUser);
            body = ApiUtil.getObjectAsPrettyJson(savedUser, "{}", responseHeaders);
        }
        catch (final JsonProcessingException | MalformedJsonParameter e) {
            exception = e;
            httpStatus = BAD_REQUEST;
        }
        finally {
            if (exception != null) ApiUtil.putExceptionInResponseHeaders(responseHeaders, exception);
        }

        return new ResponseEntity<>(
                body,
                responseHeaders,
                httpStatus);
    }

    @GetMapping("/user/{id}")
    public @NonNull ResponseEntity<String> read(@PathVariable("id") final int id) {
        final HttpHeaders responseHeaders = new HttpHeaders();
        final Optional<User> foundUser = userService.read(id);

        if (foundUser.isEmpty()) {
            return new ResponseEntity<>(
                    "{}",
                    responseHeaders,
                    NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(
                    ApiUtil.getObjectAsPrettyJson(foundUser.get(), "{}", responseHeaders),
                    responseHeaders,
                    OK);
        }
    }

    @GetMapping("/user")
    public @NonNull ResponseEntity<String> readAll(@RequestParam(required = false) final Integer page) {
        final HttpHeaders responseHeaders = new HttpHeaders();
        final List<User> users = userService.readAll();

        return new ResponseEntity<>(
                ApiUtil.getObjectAsPrettyJson(users, "[]", responseHeaders),
                responseHeaders,
                HttpStatus.OK);
    }

    @PostMapping("/user/{id}")
    public @NonNull ResponseEntity<String> update(@PathVariable("id") final int id, @RequestBody final String jsonUser) {
        final HttpHeaders responseHeaders = new HttpHeaders();
        final Optional<User> foundUser = userService.read(id);
        final User updatedUser, savedUser;

        String body = "{}";
        HttpStatus httpStatus = OK;
        Exception exception = null;

        if (foundUser.isEmpty()) {
            httpStatus = NOT_FOUND;
        }
        else {
            try {
                updatedUser = ApiUtil.getObjectMapper().readValue(jsonUser, User.class);
                if (updatedUser.getId() != null) throw new MalformedJsonParameter("Cannot specify an id in body when updating a user");
                if (!updatedUser.getFavorites().isEmpty()) throw new MalformedJsonParameter("Cannot specify favorites in body when updating a user");

                final User existingUser = foundUser.get();
                existingUser.setUsername(updatedUser.getUsername());

                savedUser = userService.update(existingUser);
                body = ApiUtil.getObjectAsPrettyJson(savedUser, "{}", responseHeaders);
            }
            catch (final JsonProcessingException | MalformedJsonParameter e) {
                exception = e;
                httpStatus = BAD_REQUEST;
            }
            finally {
                if (exception != null) ApiUtil.putExceptionInResponseHeaders(responseHeaders, exception);
            }
        }

        return new ResponseEntity<>(
                body,
                responseHeaders,
                httpStatus);
    }

    @DeleteMapping("/user/{id}")
    public @NonNull ResponseEntity<String> delete(@PathVariable("id") final int id) {
        final HttpHeaders responseHeaders = new HttpHeaders();
        final Optional<User> foundUser = userService.read(id);

        if (foundUser.isEmpty()) {
            return new ResponseEntity<>(
                    "{}",
                    responseHeaders,
                    HttpStatus.NOT_FOUND);
        }
        else {
            userService.delete(foundUser.get());

            return new ResponseEntity<>(
                    "{}",
                    responseHeaders,
                    HttpStatus.OK);
        }
    }
}
