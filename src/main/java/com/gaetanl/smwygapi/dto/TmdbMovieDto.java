package com.gaetanl.smwygapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true) // Tells jackson to ignore unknown properties when deserializing a JSON to a Title POJO
public class TmdbMovieDto {
    @JsonProperty("id")
    public int id;

    @JsonProperty("title")
    public String title;

    @JsonProperty("original_title")
    public String originalTitle;


    @JsonProperty("overview")
    public String overview;

    @JsonProperty("release_date")
    public String release_date;

    @JsonProperty("original_language")
    public String originalLanguage;

    /*
     * TMDB returns genre_ids when fetching /movie/popular, but genres when
     * fetching /movie/{id}
     */
    @JsonProperty("genre_ids")
    public List<Integer> genreIds;
    @JsonProperty("genres")
    public List<TmdbGenreDto> genres;

    @JsonProperty("adult")
    public boolean adult;

    @JsonProperty("video")
    public boolean video;


    @JsonProperty("popularity")
    public double popularity;

    @JsonProperty("vote_count")
    public int voteCount;

    @JsonProperty("vote_average")
    public double voteAverage;

    @JsonProperty("poster_path")
    public String posterPath;

    @JsonProperty("backdrop_path")
    public String backdropPath;
}
