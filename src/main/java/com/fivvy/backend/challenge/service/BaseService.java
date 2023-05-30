package com.fivvy.backend.challenge.service;

import com.fivvy.backend.challenge.dto.ResponseDTO;

public abstract class BaseService<T> {

    public ResponseDTO<T> createErrorResponse(String errorMessage) {
        return new ResponseDTO<>(null, errorMessage);
    }

    public ResponseDTO<T> createResponse(T body) {
        return new ResponseDTO<>(body, null);
    }
}
