package com.example.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@Data
public class KafkaOrderDto implements Serializable {
    private Schema schema;
    private Payload payload;
}
