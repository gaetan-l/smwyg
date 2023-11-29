package com.gaetanl.smwygapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;
import java.time.LocalDate;
import java.util.Set;

@Getter
@NoArgsConstructor
public class Title implements ModelObject {
    @Id
    private String id;

    @Setter
    private String name;

    @Setter
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "UTC")
    LocalDate releaseDate;

    @Setter
    String language;

    @Setter
    Boolean adult;

    @Setter
    private Set<Genre> genres;

    @Setter
    private String pictureUri;

    public Title(@NonNull final String id) {
        this.id = id;
    }

    @Override
    @JsonIgnore
    public @NonNull String getIdAsString() {
        return id;
    }



    // Indexes
    public enum TitleIndex implements ModelIndex<Title> {
        ID {@Override public String getIndexedValue(final Title objectToIndex) {return objectToIndex.id;}},
        NAME {@Override public String getIndexedValue(final Title objectToIndex) {return objectToIndex.name;}}
    }
}
