package com.gaetanl.smwygapi.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.Map;

public class Genre {
    private static final Logger logger = LoggerFactory.getLogger(Genre.class);

    public static Map<Integer, String> list = new HashMap<>();

    @JsonProperty("id")
    private final int id;

    @JsonProperty("name")
    private final String name;

    @JsonCreator
    public Genre(@NonNull @JsonProperty(value = "id", required = true) int id, @NonNull @JsonProperty(value = "name", required = true) String name) {
        this.id = id;
        this.name = name;

        list.put(id, name);
    }

    public static String getName(@NonNull String id) {
        return list.get(id);
    }
}
