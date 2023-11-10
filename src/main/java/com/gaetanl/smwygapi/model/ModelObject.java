package com.gaetanl.smwygapi.model;

import org.slf4j.Logger;
import org.springframework.lang.NonNull;

public interface ModelObject {
    /**
     * Returns the id as a String and checks for null (returns "null" in that
     * case). For logging purposes.
     * {@link com.gaetanl.smwygapi.util.ApiUtil#logMethodExecution(Logger, String, ModelObject)}
     *
     * @return the id as a String
     */
    @NonNull String getIdAsString();
}
