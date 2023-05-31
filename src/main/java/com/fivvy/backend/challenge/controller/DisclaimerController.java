package com.fivvy.backend.challenge.controller;

import com.fivvy.backend.challenge.dto.DisclaimerDTO;
import com.fivvy.backend.challenge.dto.ResponseDTO;
import com.fivvy.backend.challenge.model.Disclaimer;
import com.fivvy.backend.challenge.service.DisclaimerService;
import com.fivvy.backend.challenge.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/disclaimer")
public class DisclaimerController {

    @Autowired
    DisclaimerService disclaimerService;

    @GetMapping("/")
    public ResponseEntity<ResponseDTO> listDisclaimer(@RequestParam(required = false) Optional<String> text){
        Iterable<Disclaimer> disclaimers = disclaimerService.listDisclaimer(text);
        return ResponseEntity.ok(ResponseUtils.success(disclaimers));
    }
    @PostMapping("/")
    public ResponseEntity<ResponseDTO<?>> createDisclaimer(@Valid @RequestBody DisclaimerDTO dto, BindingResult result) {
        disclaimerService.checkConstraint(dto,result);
        disclaimerService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseUtils
                .success("Disclaimer created successfully."));
    }
    @PutMapping("/")
    public ResponseEntity<ResponseDTO<?>> updateDisclaimer(@RequestBody DisclaimerDTO dto){
        disclaimerService.update(dto);

        return ResponseEntity.ok(ResponseUtils.success("Disclamer updated successfully."));
    }
    @DeleteMapping("/{disclaimerId}")
    public ResponseEntity<ResponseDTO<?>> deleteDisclaimer(@PathVariable("disclaimerId") String id ){
        disclaimerService.delete(id);
        return ResponseEntity.ok(ResponseUtils.success("Disclamer deleted successfully."));
    }

}
