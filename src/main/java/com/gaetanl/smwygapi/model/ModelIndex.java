package com.gaetanl.smwygapi.model;

public interface ModelIndex<T extends ModelObject> {
    String getIndexedValue(final T objectToIndex);
}
