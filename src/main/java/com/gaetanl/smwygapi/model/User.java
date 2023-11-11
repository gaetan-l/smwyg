package com.gaetanl.smwygapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class User implements ModelObject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String username;

    @ElementCollection
    private Set<String> favorites;

    public User(final Integer id, final String username) {
        this.id = id;
        this.username = username;
        this.favorites = new HashSet<>();
    }

    @Override
    @JsonIgnore
    public @NonNull String getIdAsString() {
        return String.valueOf(id);
    }
}
