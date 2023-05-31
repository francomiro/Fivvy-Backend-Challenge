package com.fivvy.backend.challenge.exception;


import com.fivvy.backend.challenge.dto.ErrorDTO;
import com.fivvy.backend.challenge.utils.ResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AcceptanceAlreadyExistsException.class)
    public ResponseEntity<Object> handleAcceptanceAlreadyExistsException(
            AcceptanceAlreadyExistsException ex) {

        return new ResponseEntity<>(ResponseUtils.error(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DisclaimerNotFoundException.class)
    public ResponseEntity<Object> handleDisclaimerNotFoundException(
            DisclaimerNotFoundException ex) {

        return new ResponseEntity<>(ResponseUtils.error(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MissingFieldsException.class)
    public ResponseEntity<Object> handleMissingFieldsException(
            MissingFieldsException ex) {

        return new ResponseEntity<>(ResponseUtils.error(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<Object> handleNoDataFoundException(
            NoDataFoundException ex) {

        return new ResponseEntity<>(ResponseUtils.error(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DisclaimerAlreadyExistsException.class)
    public ResponseEntity<Object> handleDisclaimerAlreadyExistsException(
            DisclaimerAlreadyExistsException ex) {

        return new ResponseEntity<>(ResponseUtils.error(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }




}
