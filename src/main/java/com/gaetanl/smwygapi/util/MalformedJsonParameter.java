package com.gaetanl.smwygapi.util;

public class MalformedJsonParameter extends IllegalArgumentException {
    public MalformedJsonParameter(final String message) {
        super(message);
    }
}
