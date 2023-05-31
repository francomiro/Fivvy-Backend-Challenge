package com.fivvy.backend.challenge.controller;

import com.fivvy.backend.challenge.dto.AcceptanceDTO;
import com.fivvy.backend.challenge.dto.ResponseDTO;
import com.fivvy.backend.challenge.model.Acceptance;
import com.fivvy.backend.challenge.service.AcceptanceService;
import com.fivvy.backend.challenge.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/acceptance")
public class AcceptanceController {

    @Autowired
    AcceptanceService acceptanceService;

    @GetMapping("/")
    public ResponseEntity<ResponseDTO> listAcceptance(@RequestParam(required = false) Optional<String> userId){

        Iterable<Acceptance> listAcceptance = acceptanceService.listAcceptance(userId);
        return ResponseEntity.ok(ResponseUtils.success(listAcceptance));
    }

    @PostMapping("/")
    public ResponseEntity<ResponseDTO> createAcceptance(@Valid @RequestBody AcceptanceDTO dto, BindingResult result) {
        acceptanceService.checkConstraint(dto,result);
        acceptanceService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtils.success(
                "Acceptance created successfully."));
    }
}
