package com.foodhub.foodhub.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Error {
    private String message;
    private String code;
}
