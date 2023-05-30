package com.fivvy.backend.challenge.controller;

import com.fivvy.backend.challenge.dto.AcceptanceDTO;
import com.fivvy.backend.challenge.dto.ResponseDTO;
import com.fivvy.backend.challenge.exception.AcceptanceAlreadyExistsException;
import com.fivvy.backend.challenge.exception.DisclaimerNotFoundException;
import com.fivvy.backend.challenge.service.AcceptanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/acceptance")
public class AcceptanceController {

    @Autowired
    AcceptanceService acceptanceService;

    @GetMapping("/")
    public ResponseEntity<ResponseDTO<?>> listAcceptance(@RequestParam(required = false) String userId){
        return ResponseEntity.ok(acceptanceService.listAcceptance(userId));
    }

    @PostMapping("/")
    public ResponseEntity<ResponseDTO<?>> createAcceptance(@Valid @RequestBody AcceptanceDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(acceptanceService.createErrorResponse(
                    "BAD REQUEST - Check the fields, they must not be empty or null"));
        }
        try{
            acceptanceService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(acceptanceService.createResponse(
                    "Acceptance created successfully."));
        }catch (AcceptanceAlreadyExistsException e){
            return ResponseEntity.badRequest().body(acceptanceService.createErrorResponse(
                    e.getMessage()));
        }catch (DisclaimerNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(acceptanceService.createErrorResponse(
                    e.getMessage()));
        }
    }
}
