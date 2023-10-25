package com.gaetanl.smwygapi.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaetanl.smwygapi.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    @PutMapping(value = "/user")
    public ResponseEntity<String> createUser(@RequestBody String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        User mock;
        try {
            mock = objectMapper.readValue(json, User.class);
        }
        catch (JsonProcessingException e) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("exception", e.getMessage());

            return new ResponseEntity<String>(
                    "(Mock) Couldn't create user",
                    responseHeaders,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("user", json);

        return new ResponseEntity<String>(
                String.format("(Mock) Created user with id: %d and username: '%s'",mock.getId(), mock.getUsername()),
                responseHeaders,
                HttpStatus.CREATED);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<String> readUser(@PathVariable("id") int id) {
        boolean found = id > 0;

        if (!found) {
            return new ResponseEntity<String>(
                    String.format("(Mock) Couldn't read user with id: %d", id),
                    null,
                    HttpStatus.NOT_FOUND);
        }
        else {
            User mock = new User(id, "Mock");

            ObjectMapper objectMapper = new ObjectMapper();
            String json;
            try {
                json = objectMapper.writeValueAsString(mock);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("user", json);

            return new ResponseEntity<String>(
                    String.format("(Mock) Read user with id: %d", id),
                    responseHeaders,
                    HttpStatus.OK);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<String> readUsers() {
        List<User> mock = new ArrayList<User>();
        for (int i = 0 ; i < 10 ; i++) {
            mock.add(new User(i, String.format("Mock %d", i)));
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String json;
        try {
            json = objectMapper.writeValueAsString(mock);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("users", json);

        return new ResponseEntity<String>(
                "(Mock) Read all users",
                responseHeaders,
                HttpStatus.OK);
    }

    @PostMapping("/user/{id}")
    public ResponseEntity<String> updateUser(@PathVariable("id") int id, @RequestBody String json) {
        boolean found = id == 42;

        if (!found) {
            return new ResponseEntity<String>(
                    String.format("(Mock) Couldn't read user with id: %d", id),
                    null,
                    HttpStatus.NOT_FOUND);
        }
        else {
            User foundMock = new User(42, "oldUserName");

            ObjectMapper objectMapper = new ObjectMapper();
            HttpHeaders responseHeaders = new HttpHeaders();
            User updatedMock;
            try {
                updatedMock = objectMapper.readValue(json, User.class);
            }
            catch (JsonProcessingException e) {
                responseHeaders.set("exception", e.getMessage());

                return new ResponseEntity<String>(
                        String.format("(Mock) Couldn't update user with id: %d", id),
                        responseHeaders,
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<String>(
                    String.format("(Mock) Updated user with id: %d, username changed from '%s' to '%s'", id, foundMock.getUsername(), updatedMock.getUsername()),
                    responseHeaders,
                    HttpStatus.OK);
        }
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") int id) {
        boolean found = id == 42;

        if (!found) {
            return new ResponseEntity<String>(
                    String.format("(Mock) Couldn't read user with id: %d", id),
                    null,
                    HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<String>(
                    String.format("(Mock) Deleted user with id: %d", id),
                    null,
                    HttpStatus.OK);
        }
    }
}