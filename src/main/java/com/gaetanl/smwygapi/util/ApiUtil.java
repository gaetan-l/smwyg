package com.gaetanl.smwygapi.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gaetanl.smwygapi.model.ModelObject;
import lombok.Getter;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import java.util.Arrays;

public final class ApiUtil {
    private ApiUtil() {}

    @Getter
    private static final ObjectMapper objectMapper = (new ObjectMapper()).registerModule(new JavaTimeModule());

    /**
     * Adds a given exception to an HTTP header with the following structure:
     * Exception: {
     *     simpleName: exception.getClass().getSimpleName()
     *     name:       exception.getClass().getName()
     *     message:    exception.getMessage()
     *     stacktrace: [
     *       stacktraceElement,
     *       stacktraceElement,
     *       ...]}
     *
     * @param  responseHeaders  the HTTP headers in which to add the exception details
     * @param  exception        the exception to add
     */
    public static void putExceptionInResponseHeaders(final HttpHeaders responseHeaders, final Exception exception) {
        responseHeaders.set(
                "Exception",
                String.format(
                        "{\"simpleName\":\"%s\",\"name\":\"%s\",\"message\":\"%s\",\"stacktrace\":[%s]\r\n}",
                        exception.getClass().getSimpleName(),
                        exception.getClass().getName(),
                        exception.getMessage(),
                        Arrays.asList(exception.getStackTrace())
                )
        );
    }

    /**
     * Returns an object as a pretty JSON string. If an exception arises in the
     * JSON process, silently fails and puts the exception in the HTTP header
     * instead.
     * <p>
     * This function is meant to be called in REST controllers, when the
     * writing of an object as JSON in the response body is optional and can
     * fail without compromising the rest of the process (for example, after
     * creating a user, and wanting to display it in the body just as info).
     *
     * @param  object           the object to write as JSON
     * @param  emptyValue       the string to return in case the JSON
     *                          processing fails, for example "{}" or "[]"
     * @param  responseHeaders  the HTTP header of the response that is going
     *                          to be called next
     * @return                  the pretty JSON corresponding to the serialized
     *                          object
     */
    public static String getObjectAsPrettyJson(final Object object, final String emptyValue, final HttpHeaders responseHeaders) {
        String prettyJson = emptyValue;

        try {
            prettyJson = getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
        }
        catch (final JsonProcessingException e) {
            putExceptionInResponseHeaders(responseHeaders, e);
        }

        return prettyJson;
    }

    public static void logMethodExecution(final Logger logger, final String method) {
        logMethodExecution(logger, method, "");
    }

    public static void logMethodExecution(final Logger logger, final String method, final ModelObject interactedObject) {
        logMethodExecution(logger, method, null == interactedObject ? "" : String.format(
                "{%s with id=%s}",
                interactedObject.getClass().getSimpleName(),
                interactedObject.getIdAsString()));
    }

    public static void logMethodExecution(final Logger logger, final String method, final String details) {
        logger.info(String.format(
                "%s(%s)",
                method,
                details));
    }
}
