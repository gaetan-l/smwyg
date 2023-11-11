package com.gaetanl.smwygapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;

@SuppressWarnings("unused")
public abstract class TmdbMovieDto {
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

    @JsonIgnore
    public abstract Set<Integer> getGenreIdsSet();
}
