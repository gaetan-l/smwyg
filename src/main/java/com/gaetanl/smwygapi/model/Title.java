package com.gaetanl.smwygapi.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Title {
    @Id
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("keywords")
    @ElementCollection
    @CollectionTable
    private Set<String> keywords;

    @JsonCreator
    public Title(@Nullable @JsonProperty("id") String id,
                 @NonNull @JsonProperty(value = "name", required = true) String name,
                 @NonNull @JsonProperty(value = "keywords", required = true) Set<String> keywords) {
        this.id = id;
        this.name = name;
        this.keywords = keywords;
    }

    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public @NonNull String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public @NonNull Set<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(@NonNull Set<String> keywords) {
        this.keywords = keywords;
    }
}
