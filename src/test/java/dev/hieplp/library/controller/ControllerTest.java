package dev.hieplp.library.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class ControllerTest {
    protected final String EMPTY_CONTENT = "{}";

    protected String toJson(final Object obj) throws JsonProcessingException {
        var objectMapper = new ObjectMapper();

        // Configure ObjectMapper to use a custom date format
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        return objectMapper.writeValueAsString(obj);
    }
}
