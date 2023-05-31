package com.fivvy.backend.challenge.service;

import com.fivvy.backend.challenge.dto.DisclaimerDTO;
import com.fivvy.backend.challenge.model.Disclaimer;
import org.springframework.validation.BindingResult;

import java.util.Optional;

public interface DisclaimerService {
    Iterable<Disclaimer> listDisclaimer(Optional<String> text);

    Disclaimer create(DisclaimerDTO disclaimerDTO);

    Disclaimer update(DisclaimerDTO disclaimerDTO);

    void delete(String id);

    void checkConstraint(DisclaimerDTO dto, BindingResult result);
}
