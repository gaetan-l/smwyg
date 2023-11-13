package com.gaetanl.smwygapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;
import java.util.Set;

@Getter
@NoArgsConstructor
public class Title implements ModelObject {
    @Id
    private String id;

    private String name;

    @Setter
    private Set<Genre> genres;

    @Setter
    private String pictureUri;

    public Title(final String id, final String name, final Set<Genre> genres, final String pictureUri) {
        this.id = id;
        this.name = name;
        this.genres = genres;
        this.pictureUri = pictureUri;
    }

    @Override
    @JsonIgnore
    public @NonNull String getIdAsString() {
        return id;
    }



    // Indexes
    @SuppressWarnings("unused") // Indexes are instanced using strings passed to rest controllers
    public enum TitleIndex implements ModelIndex<Title> {
        ID {@Override public String getIndexedValue(final Title objectToIndex) {return objectToIndex.id;}},
        NAME {@Override public String getIndexedValue(final Title objectToIndex) {return objectToIndex.name;}}
    }
}
