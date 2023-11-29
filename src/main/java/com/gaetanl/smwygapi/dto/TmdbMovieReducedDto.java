package com.gaetanl.smwygapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused") // Used implicitly by Jackson
@JsonIgnoreProperties(ignoreUnknown = true) // Tells jackson to ignore unknown properties when deserializing a JSON to a Title POJO
public class TmdbMovieReducedDto extends TmdbMovieDto {
    /*
     * About field being public and @SuppressWarnings("WeakerAccess") :
     * Voluntary lack of safety on DTO classes for ease of use and code
     * readability. This warning appears because field is returned in
     * {@link com.gaetanl.smwygapi.dto.TmdbMovieDto#getGenreIdsSet()}
     */
    @JsonProperty("genre_ids")
    @SuppressWarnings("WeakerAccess") // Voluntary lack of safety on DTO (see above)
    public List<Integer> genreIds;

    @Override
    public Set<Integer> getGenreIdsSet() {
        return new HashSet<>(genreIds);
    }
}
