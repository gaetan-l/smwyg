package com.gaetanl.smwygapi.model;

import org.slf4j.Logger;
import org.springframework.lang.NonNull;
import java.util.HashSet;
import java.util.Set;

public interface ModelObject {
    /**
     * Returns the id as a String and checks for null (returns "null" in that
     * case). For logging purposes.
     * {@link com.gaetanl.smwygapi.util.ApiUtil#logMethodExecution(Logger, String, ModelObject)}
     *
     * @return the id as a String
     */
    @NonNull String getIdAsString();

    /**
     * Returns the set of indexes available to this model object. To be
     * overloaded in classes implementing ModelObject. Never meant to be called
     * from ModelObject, hence @SuppressWarnings("unused").
     *
     * @return the set of indexes available for this model object
     */
    @SuppressWarnings("unused") // Method always overloaded by child classes
    @NonNull static Set<ModelIndex<ModelObject>> getIndexes() {
        return new HashSet<>();
    }
}
