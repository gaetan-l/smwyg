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

@Entity
@Getter
@NoArgsConstructor
public class Title implements ModelObject {
    @Id
    private String id;

    @Setter
    private String name;

    @Setter
    @ElementCollection
    @CollectionTable
    private Set<String> genres;

    @Setter
    private String pictureUri;

    public Title(final String id, final String name, final Set<String> genres, final String pictureUri) {
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
}
