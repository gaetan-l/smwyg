package com.gaetanl.smwygapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@JsonInclude(JsonInclude.Include.NON_NULL) // Set absent fields to default null value when deserializing
public class SmwygSearchParametersDto {
    public boolean include_adult = false;
    public Integer primary_release_year;
    public List<String> with_genres;
    public List<String> with_keywords;
    public String with_origin_country;
    public String with_original_language;
}
