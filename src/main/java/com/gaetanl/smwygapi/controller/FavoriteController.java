package com.gaetanl.smwygapi.controller;

import com.gaetanl.smwygapi.model.Favorite;
import com.gaetanl.smwygapi.model.Title;
import com.gaetanl.smwygapi.model.User;
import com.gaetanl.smwygapi.service.FavoriteService;
import com.gaetanl.smwygapi.service.TitleService;
import com.gaetanl.smwygapi.service.UserService;
import com.gaetanl.smwygapi.util.ApiUtil;
import com.gaetanl.smwygapi.util.DuplicateEntityException;
import com.gaetanl.smwygapi.util.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.*;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
public class FavoriteController {
    @Autowired
    UserService userService;

    @Autowired
    TitleService titleService;

    @Autowired
    FavoriteService favoriteService;

    @PutMapping("/user/{userId}/favorite/{titleId}")
    public @NonNull ResponseEntity<String> create(@PathVariable("userId") final int userId, @PathVariable("titleId") final String titleId) {
        final HttpHeaders responseHeaders = new HttpHeaders();
        final Optional<User> foundUser = userService.read(userId);
        final Optional<Title> foundTitle;

        String body = "{}";
        HttpStatus httpStatus = CREATED;
        Exception exception = null;

        try {
            if (foundUser.isEmpty()) throw new EntityNotFoundException(User.class, String.valueOf(userId));
            body = ApiUtil.getObjectAsPrettyJson(foundUser.get(), "{}", responseHeaders);

            foundTitle = titleService.read(titleId);
            if (foundTitle.isEmpty()) throw new EntityNotFoundException(Title.class, titleId);

            if (foundUser.get().getFavorites().contains(foundTitle.get().getId()))
                throw new DuplicateEntityException(Favorite.class, foundUser.get().getIdAsString() + "+" + foundTitle.get().getName());

            final User savedUser = favoriteService.create(foundUser.get(), foundTitle.get());
            body = ApiUtil.getObjectAsPrettyJson(savedUser, "{}", responseHeaders);
        }
        catch (final WebClientResponseException | URISyntaxException | EntityNotFoundException e) {
            exception = e;
            httpStatus = NOT_FOUND;
        }
        catch (final IOException e) {
            exception = e;
            httpStatus = INTERNAL_SERVER_ERROR;
        }
        catch (final DuplicateEntityException e) {
            exception = e;
            httpStatus = UNPROCESSABLE_ENTITY;
        }
        finally {
            if (exception != null) ApiUtil.putExceptionInResponseHeaders(responseHeaders, exception);
        }

        return new ResponseEntity<>(
                body,
                responseHeaders,
                httpStatus);
    }

    @DeleteMapping("/user/{userId}/favorite/{titleId}")
    public @NonNull ResponseEntity<String> delete(@PathVariable("userId") final int userId, @PathVariable("titleId") final String titleId) {
        final HttpHeaders responseHeaders = new HttpHeaders();
        final Optional<User> foundUser = userService.read(userId);
        final Optional<Title> foundTitle;

        String body = "{}";
        HttpStatus httpStatus = OK;
        Exception exception = null;

        try {
            if (foundUser.isEmpty()) throw new EntityNotFoundException(User.class, String.valueOf(userId));
            body = ApiUtil.getObjectAsPrettyJson(foundUser.get(), "{}", responseHeaders);

            foundTitle = titleService.read(titleId);
            if (foundTitle.isEmpty()) throw new EntityNotFoundException(Title.class, titleId);

            if (!foundUser.get().getFavorites().contains(foundTitle.get().getId()))
                throw new EntityNotFoundException(Favorite.class, foundUser.get().getIdAsString() + "+" + foundTitle.get().getName());

            final User savedUser = favoriteService.delete(foundUser.get(), foundTitle.get());
            body = ApiUtil.getObjectAsPrettyJson(savedUser, "{}", responseHeaders);
        }
        catch (final WebClientResponseException | URISyntaxException | EntityNotFoundException e) {
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
