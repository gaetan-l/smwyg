package com.gaetanl.smwygapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class Genre implements ModelObject {
    private Integer id;
    private String name;

    @Override
    @JsonIgnore
    public @NonNull String getIdAsString() {
        return String.valueOf(id);
    }



    // Indexes
    @SuppressWarnings("unused")
    // Indexes are instanced using strings passed to rest controllers
    public enum GenreIndex implements ModelIndex<Genre> {
        ID {@Override public String getIndexedValue(final Genre objectToIndex) {return String.valueOf(objectToIndex.id);}},
        NAME {@Override public String getIndexedValue(final Genre objectToIndex) {return objectToIndex.name;}}
    }

    //  @Overload
    public static @NonNull Set<ModelIndex<Genre>> getIndexes() {
        return new HashSet<>(Arrays.asList(Genre.GenreIndex.values()));
    }
}
