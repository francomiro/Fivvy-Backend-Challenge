package com.fivvy.backend.challenge.controller;

import com.fivvy.backend.challenge.dto.DisclaimerDTO;
import com.fivvy.backend.challenge.dto.ResponseDTO;
import com.fivvy.backend.challenge.exception.DisclaimerAlreadyExistsException;
import com.fivvy.backend.challenge.exception.DisclaimerNotFoundException;
import com.fivvy.backend.challenge.service.DisclaimerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/disclaimer")
public class DisclaimerController {

    @Autowired
    DisclaimerService disclaimerService;

    @GetMapping("/")
    public ResponseEntity<ResponseDTO<?>> listDisclaimer(@RequestParam(required = false) String text){
        return ResponseEntity.ok(disclaimerService.listDisclaimer(text));
    }
    @PostMapping("/")
    public ResponseEntity<ResponseDTO<?>> createDisclaimer(@Valid @RequestBody DisclaimerDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(disclaimerService.createErrorResponse(
                    "BAD REQUEST - Check the fields, they must not be empty or null"));
        }
        try{
            disclaimerService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(disclaimerService.createResponse(
                    "Disclaimer created successfully."));

        }catch (DisclaimerAlreadyExistsException e){
            return ResponseEntity.badRequest().body(disclaimerService.createErrorResponse(
                    e.getMessage()));
        }
    }
    @PutMapping("/")
    public ResponseEntity<ResponseDTO<?>> updateDisclaimer(@RequestBody DisclaimerDTO dto) throws DisclaimerNotFoundException{
        try{
            disclaimerService.update(dto);
            return ResponseEntity.ok(disclaimerService.createResponse("Disclamer updated successfully."));
        } catch (DisclaimerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(disclaimerService.createErrorResponse(e.getMessage()));
        }

    }
    @DeleteMapping("/{disclaimerId}")
    public ResponseEntity<ResponseDTO<?>> deleteDisclaimer(@PathVariable("disclaimerId") String id ) throws DisclaimerNotFoundException {
        try {
            disclaimerService.delete(id);
            return ResponseEntity.ok(disclaimerService.createResponse("Disclamer deleted successfully."));
        } catch (DisclaimerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(disclaimerService.createErrorResponse(e.getMessage()));
        }
    }

}
