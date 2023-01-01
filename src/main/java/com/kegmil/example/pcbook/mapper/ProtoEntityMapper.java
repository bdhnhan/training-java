package com.kegmil.example.pcbook.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProtoEntityMapper {

    private static ObjectMapper mapper = new ObjectMapper();

    public static <T extends Message, U> U toEntity(T source, Class<U> desc) throws IOException {
        String json = JsonFormat.printer().omittingInsignificantWhitespace().includingDefaultValueFields().print(source);
        U obj = mapper.readValue(json, desc);
        return obj;
    }

    public static <T extends Message, U> List<U> toEntities(List<T> source, Class<U> desc) throws IOException {
        List ls = new ArrayList();
        for (T t : source) {
            ls.add(toEntity(t, desc));
        }

        return ls;
    }

    public static <T, U> U toEntity(T source, Class<U> desc) throws IOException {
        String json = mapper.writeValueAsString(source);
        U obj = mapper.readValue(json, desc);
        return obj;
    }

    public static <T, U extends GeneratedMessageV3.Builder> U toProto(T source, U desc) throws IOException {
        String json = mapper.writeValueAsString(source);
        JsonFormat.parser().ignoringUnknownFields().merge(json, desc);
        return desc;
    }

    public static <T extends Message, U extends GeneratedMessageV3.Builder> U toProto(T source, U desc) throws IOException {
        String json = JsonFormat.printer().omittingInsignificantWhitespace().includingDefaultValueFields().print(source);
        JsonFormat.parser().ignoringUnknownFields().merge(json, desc);
        return desc;
    }

    public static ObjectMapper getObjectMapper() {
        return mapper;
    }
}
