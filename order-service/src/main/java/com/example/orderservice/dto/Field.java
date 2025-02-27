package com.example.orderservice.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class Field {
    private String type;
    private boolean optional;
    private String field;
}
