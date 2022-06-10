package com.epam.esm.entity;

import java.util.Map;

public record ErrorInfo(String errorMessage,
                        Map<String, String> fieldError,
                        int errorCode) {
}
