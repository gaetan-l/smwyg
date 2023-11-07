package com.gaetanl.smwygapi.controller;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaetanl.smwygapi.util.ApiUtil;
import com.gaetanl.smwygapi.model.User;
import com.gaetanl.smwygapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PutMapping(value = "/user")
    public ResponseEntity<String> createUser(@RequestBody String jsonUser) {
        HttpHeaders responseHeaders = new HttpHeaders();
        String body = "{}";

        ObjectMapper objectMapper = new ObjectMapper();
        User savedUser;

        try {
            savedUser = objectMapper.readValue(jsonUser, User.class);
            if (savedUser.getId() != null) throw new IllegalArgumentException("Cannot specify an id in body when creating a user");
            savedUser = userRepository.save(savedUser);
        }
        catch (JacksonException | IllegalArgumentException e) {
            ApiUtil.putExceptionInResponseHeaders(responseHeaders, e);

            return new ResponseEntity<String>(
                    body,
                    responseHeaders,
                    HttpStatus.BAD_REQUEST);
        }

        try {
            body = objectMapper.writeValueAsString(savedUser);
        }
        catch (JacksonException e) {
            ApiUtil.putExceptionInResponseHeaders(responseHeaders, e);
            // Continue
        }

        return new ResponseEntity<String>(
                body,
                responseHeaders,
                HttpStatus.CREATED);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<String> readUser(@PathVariable("id") int id) {
        HttpHeaders responseHeaders = new HttpHeaders();
        String body = "{}";

        Optional<User> foundResult = userRepository.findById(id);

        if (foundResult.isEmpty()) {
            return new ResponseEntity<String>(
                    body,
                    responseHeaders,
                    HttpStatus.NOT_FOUND);
        }
        else {
            User foundUser = foundResult.get();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                body = objectMapper.writeValueAsString(foundUser);
            }
            catch (JacksonException e) {
                ApiUtil.putExceptionInResponseHeaders(responseHeaders, e);
                // Continue
            }

            return new ResponseEntity<String>(
                    body,
                    responseHeaders,
                    HttpStatus.OK);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<String> readUsers() {
        HttpHeaders responseHeaders = new HttpHeaders();
        String body = "[]";

        Iterable<User> users = userRepository.findAll();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            body = objectMapper.writeValueAsString(users);
        }
        catch (JacksonException e) {
            ApiUtil.putExceptionInResponseHeaders(responseHeaders, e);
            // Continue
        }

        return new ResponseEntity<String>(
                body,
                responseHeaders,
                HttpStatus.OK);
    }

    @PostMapping("/user/{id}")
    public ResponseEntity<String> updateUser(@PathVariable("id") int id, @RequestBody String json) {
        HttpHeaders responseHeaders = new HttpHeaders();
        String body = "{}";

        ObjectMapper objectMapper = new ObjectMapper();
        User updatedUser;

        Optional<User> foundResult = userRepository.findById(id);

        if (foundResult.isEmpty()) {
            return new ResponseEntity<String>(
                    body,
                    responseHeaders,
                    HttpStatus.NOT_FOUND);
        }
        else {
            try {
                updatedUser = objectMapper.readValue(json, User.class);
                if (updatedUser.getId() != null) throw new IllegalArgumentException("Cannot specify an id in body when updating a user");
                int foundId = foundResult.get().getId();
                updatedUser.setId(foundId);
                updatedUser = userRepository.save(updatedUser);
            }
            catch (JacksonException | IllegalArgumentException e) {
                ApiUtil.putExceptionInResponseHeaders(responseHeaders, e);

                return new ResponseEntity<String>(
                        body,
                        responseHeaders,
                        HttpStatus.BAD_REQUEST);
            }

            try {
                body = objectMapper.writeValueAsString(updatedUser);
            }
            catch (JacksonException e) {
                ApiUtil.putExceptionInResponseHeaders(responseHeaders, e);
                // Continue
            }

            return new ResponseEntity<String>(
                    body,
                    responseHeaders,
                    HttpStatus.OK);
        }
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") int id) {
        HttpHeaders responseHeaders = new HttpHeaders();
        String body = "{}";

        ObjectMapper objectMapper = new ObjectMapper();

        Optional<User> foundResult = userRepository.findById(id);

        if (foundResult.isEmpty()) {
            return new ResponseEntity<String>(
                    body,
                    responseHeaders,
                    HttpStatus.NOT_FOUND);
        }
        else {
            userRepository.delete(foundResult.get());

            return new ResponseEntity<String>(
                    body,
                    responseHeaders,
                    HttpStatus.OK);
        }
    }
}