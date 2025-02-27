package com.example.orderservice.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Schema {
    private String type;
    private List<Field> fields = new ArrayList<>();
    private boolean optional;
    private String name;

    public Schema(String name, Field... fields) {
        this.type = "struct";
        this.fields = Arrays.stream(fields).collect(Collectors.toList());
        this.optional = false;
        this.name = name;
    }

}
