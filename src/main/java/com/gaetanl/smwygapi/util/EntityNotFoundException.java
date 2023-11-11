package com.gaetanl.smwygapi.util;

import com.gaetanl.smwygapi.model.ModelObject;

public class EntityNotFoundException extends IllegalArgumentException{
    public EntityNotFoundException(final Class<? extends ModelObject> entityClass, final String id) {
        super(String.format("{%s with id=%s} not found", entityClass.getSimpleName(), id));
    }
}
