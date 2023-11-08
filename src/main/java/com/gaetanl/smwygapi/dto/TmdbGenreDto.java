package com.gaetanl.smwygapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TmdbGenreDto {
    @JsonProperty("id")
    public int id;

    @JsonProperty("name")
    public String name;
}
