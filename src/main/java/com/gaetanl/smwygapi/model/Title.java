package com.gaetanl.smwygapi.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Title {
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

    public Title(String id, String name, Set<String> genres, String pictureUri) {
        this.id = id;
        this.name = name;
        this.genres = genres;
        this.pictureUri = pictureUri;
    }
}
