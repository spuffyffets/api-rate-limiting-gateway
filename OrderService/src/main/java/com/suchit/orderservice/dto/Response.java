package com.suchit.orderservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {

    private Integer status;

    private String message;

    private Object data;
}