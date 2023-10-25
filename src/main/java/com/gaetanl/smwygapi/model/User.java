package com.gaetanl.smwygapi.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

public class User {
    @JsonProperty("id")
    private int id;
    @JsonProperty("username")
    private String username;

    @JsonCreator
    public User(@NonNull @JsonProperty("id") Integer id, @NonNull @JsonProperty(value = "username", required = true) String username) {
        this.id = id;
        this.username = username;
    }

    public @NonNull int getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    public @NonNull String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }
}
