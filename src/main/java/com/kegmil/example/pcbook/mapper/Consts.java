package com.kegmil.example.pcbook.mapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Consts {
    public static final ObjectMapper JACKSON_MAPPER = new ObjectMapper();

    public Consts() {
    }

    static {
        JACKSON_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JACKSON_MAPPER.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
        JACKSON_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }
}
