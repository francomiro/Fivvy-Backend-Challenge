package com.fivvy.backend.challenge.service;

import com.fivvy.backend.challenge.dto.AcceptanceDTO;
import com.fivvy.backend.challenge.model.Acceptance;
import org.springframework.validation.BindingResult;

import java.util.Optional;

public interface AcceptanceService {
    Iterable<Acceptance> listAcceptance(Optional<String> userId);
    Acceptance create(AcceptanceDTO dto);
    void checkConstraint(AcceptanceDTO dto, BindingResult result);
}
