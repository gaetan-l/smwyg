package com.gaetanl.smwygapi.model;

import org.springframework.lang.NonNull;

// TODO: Should this be a class ? Warning, used in {@link com.gaetanl.smwygapi.util.DuplicateEntityException}
public class Favorite implements ModelObject {
    @Override
    public @NonNull String getIdAsString() {
        return null;
    }
}
