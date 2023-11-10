package com.gaetanl.smwygapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gaetanl.smwygapi.model.User;
import com.gaetanl.smwygapi.service.UserService;
import com.gaetanl.smwygapi.util.ApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

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

        try {
            newUser = ApiUtil.getObjectMapper().readValue(jsonUser, User.class);
            if (newUser.getId() != null) throw new IllegalArgumentException("Cannot specify an id in body when creating a user");
            savedUser = userService.create(newUser);
        }
        catch (final JsonProcessingException | IllegalArgumentException e) {
            ApiUtil.putExceptionInResponseHeaders(responseHeaders, e);

            return new ResponseEntity<>(
                    "{}",
                    responseHeaders,
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(
                ApiUtil.getObjectAsPrettyJson(savedUser, "{}", responseHeaders),
                responseHeaders,
                HttpStatus.CREATED);
    }

    @GetMapping("/user/{id}")
    public @NonNull ResponseEntity<String> read(@PathVariable("id") final int id) {
        final HttpHeaders responseHeaders = new HttpHeaders();
        final Optional<User> foundUser = userService.read(id);

        if (foundUser.isEmpty()) {
            return new ResponseEntity<>(
                    "{}",
                    responseHeaders,
                    HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(
                    ApiUtil.getObjectAsPrettyJson(foundUser.get(), "{}", responseHeaders),
                    responseHeaders,
                    HttpStatus.OK);
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

        if (foundUser.isEmpty()) {
            return new ResponseEntity<>(
                    "{}",
                    responseHeaders,
                    HttpStatus.NOT_FOUND);
        }
        else {
            try {
                updatedUser = ApiUtil.getObjectMapper().readValue(jsonUser, User.class);
                if (updatedUser.getId() != null) throw new IllegalArgumentException("Cannot specify an id in body when updating a user");
                final int existingId = foundUser.get().getId();

                savedUser = userService.update(new User(existingId, updatedUser.getUsername()));
            }
            catch (final JsonProcessingException | IllegalArgumentException e) {
                ApiUtil.putExceptionInResponseHeaders(responseHeaders, e);

                return new ResponseEntity<>(
                        "{}",
                        responseHeaders,
                        HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(
                    ApiUtil.getObjectAsPrettyJson(savedUser, "{}", responseHeaders),
                    responseHeaders,
                    HttpStatus.OK);
        }
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
