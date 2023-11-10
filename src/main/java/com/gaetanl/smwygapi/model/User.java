package com.gaetanl.smwygapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Entity
@Getter
@NoArgsConstructor
public class User implements ModelObject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String username;

    public User(final Integer id, final String username) {
        this.id = id;
        this.username = username;
    }

    @Override
    @JsonIgnore
    public @NonNull String getIdAsString() {
        return String.valueOf(id);
    }
}
