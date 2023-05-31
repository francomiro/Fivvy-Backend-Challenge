package com.fivvy.backend.challenge.utils;

import com.fivvy.backend.challenge.dto.ErrorDTO;
import com.fivvy.backend.challenge.dto.ResponseDTO;

import java.time.LocalDateTime;

public class ResponseUtils {
    public static <T> ResponseDTO<T> success(T body) {
        return new ResponseDTO<>(body, null);
    }

    public static ResponseDTO<?> error(String errorMessage) {
        ErrorDTO error = new ErrorDTO(errorMessage,LocalDateTime.now());
        return new ResponseDTO<>(null, error);
    }
}
