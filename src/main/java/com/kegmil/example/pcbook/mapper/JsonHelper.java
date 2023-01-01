package com.kegmil.example.pcbook.mapper;

import com.fasterxml.jackson.databind.JavaType;
import com.google.protobuf.util.JsonFormat;
import com.kegmil.example.pcbook.pb.Laptop;

import java.io.IOException;
import java.util.function.Function;

public class JsonHelper {
    public static String toJsonString(Object sourceObject) {
        try {
            return Consts.JACKSON_MAPPER.writeValueAsString(sourceObject);
        } catch (IOException var2) {
            throw new IllegalArgumentException(var2);
        }
    }


    public static String mapLaptopProtoToJson(Laptop laptop) {
        try {
            return JsonFormat.printer().print(laptop);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <T> Function<String, T> getMapper(Class<T> clazz) {
        return (json) -> {
            try {
                JavaType type = Consts.JACKSON_MAPPER.getTypeFactory().constructType(clazz);
                return Consts.JACKSON_MAPPER.readValue(json, type);
            } catch (IOException var3) {
                throw new RuntimeException(var3.getMessage(), var3);
            }
        };
    }
}
