package com.suchit.apigateway.ratelimit.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {

    private Integer status;

    private String message;

    private Object data;
}