package com.gaetanl.smwygapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused") // Used implicitly by Jackson
@JsonIgnoreProperties(ignoreUnknown = true) // Tells jackson to ignore unknown properties when deserializing a JSON to a Title POJO
public class TmdbMovieDetailsDto extends TmdbMovieDto {
    @JsonProperty("mdb_id")
    public String imdbId;

    @JsonProperty("tagline")
    public String tagline;
/*
    TODO: Implement TMDB language property
    @JsonProperty("spoken_languages")
    public List<Language> spokenLanguages;
*/
    /*
     * About field being public and @SuppressWarnings("WeakerAccess") :
     * Voluntary lack of safety on DTO classes for ease of use and code
     * readability. This warning appears because field is returned in
     * {@link com.gaetanl.smwygapi.dto.TmdbMovieDto#getGenreIdsSet()}
     */
    @JsonProperty("genres")
    @SuppressWarnings("WeakerAccess") // Voluntary lack of safety on DTO (see above)
    public List<TmdbGenreDto> genres;

    @JsonProperty("runtime")
    public int runtime;

    @JsonProperty("status")
    public String status;


    @JsonProperty("homepage")
    public String homepage;
/*
    TODO: Implement TMDB production company and country
    @JsonProperty("production_companies")
    public List<ProductionCompany> productionCompanies;

    @JsonProperty("production_countries")
    public list<Country> productionCountries;
*/
    @JsonProperty("budget")
    public int budget;

    @JsonProperty("revenue")
    public int revenue;

/*
    TODO: Implement TMDB collection property
    @JsonProperty("belongs_to_collection")
    public String belongsToCollection;
*/
    @Override
    public Set<Integer> getGenreIdsSet() {
        return genres.stream().map(tmdbGenreDto -> tmdbGenreDto.id).collect(Collectors.toSet());
    }
}
