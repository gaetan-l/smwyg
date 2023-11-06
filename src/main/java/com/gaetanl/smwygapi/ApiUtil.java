package com.gaetanl.smwygapi;

import org.springframework.http.HttpHeaders;

import java.util.Arrays;

public final class ApiUtil {
    private ApiUtil() {}

    public static void putExceptionInResponseHeaders(HttpHeaders responseHeaders, Exception exception) {
        responseHeaders.set(
                "Exception",
                String.format("{\"simpleName\": \"%s\", \"name\": \"%s\", \"message\": \"%s\", \"stacktrace\":\"%s\"}",
                        exception.getClass().getSimpleName(),
                        exception.getClass().getName(),
                        exception.getMessage(),
                        Arrays.toString(exception.getStackTrace())));
    }
}
