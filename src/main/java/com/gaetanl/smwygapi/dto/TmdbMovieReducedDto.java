package com.gaetanl.smwygapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true) // Tells jackson to ignore unknown properties when deserializing a JSON to a Title POJO
public class TmdbMovieReducedDto extends TmdbMovieDto {
    @JsonProperty("genre_ids")
    @SuppressWarnings("WeakerAccess")
    public List<Integer> genreIds;

    @Override
    public Set<Integer> getGenreIdsSet() {
        return new HashSet<>(genreIds);
    }
}
