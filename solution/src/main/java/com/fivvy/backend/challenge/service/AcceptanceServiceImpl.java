package com.fivvy.backend.challenge.service;

import com.fivvy.backend.challenge.dto.AcceptanceDTO;
import com.fivvy.backend.challenge.exception.AcceptanceAlreadyExistsException;
import com.fivvy.backend.challenge.exception.DisclaimerNotFoundException;
import com.fivvy.backend.challenge.exception.MissingFieldsException;
import com.fivvy.backend.challenge.exception.NoDataFoundException;
import com.fivvy.backend.challenge.model.Acceptance;
import com.fivvy.backend.challenge.repository.AcceptanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Optional;


@Service
public class AcceptanceServiceImpl implements AcceptanceService {

    @Autowired
    AcceptanceRepository acceptanceRepository;

    @Autowired
    DisclaimerServiceImpl disclaimerService;

    @Override
    public Iterable<Acceptance> listAcceptance(Optional<String> userId) {
        Iterable<Acceptance> listAcceptance;
        if (!userId.isPresent()) {
            listAcceptance = findAll();
        } else {
            listAcceptance = findByUser(userId.get());
        }
        return listAcceptance;
    }

    @Override
    public Acceptance create(AcceptanceDTO dto){
        Acceptance acceptance = new Acceptance();
        acceptance.setUserId(dto.getUserId());
        acceptance.setDisclaimerId(dto.getDisclaimerId());
        return acceptanceRepository.save(acceptance);
    }

    @Override
    public void checkConstraint(AcceptanceDTO dto, BindingResult result) throws AcceptanceAlreadyExistsException, DisclaimerNotFoundException {
        if (result.hasErrors()) {
            throw new MissingFieldsException("Check the fields, they must not be empty or null");
        }
        if (existsByDisclaimerAndUser(dto.getDisclaimerId(),dto.getUserId())){
            throw new AcceptanceAlreadyExistsException("Acceptance already exists for that user and disclaimer.");
        }
        if (!disclaimerService.existsById(dto.getDisclaimerId())){
            throw new DisclaimerNotFoundException("Disclaimer ID not found.");
        }
    }

    private Iterable<Acceptance> findAll() {
        Iterable<Acceptance> listAcceptance = acceptanceRepository.findAll();
        if(!listAcceptance.iterator().hasNext()){
            throw new NoDataFoundException("There isn't any acceptance to show");
        }
        return listAcceptance;
    }

    private Iterable<Acceptance> findByUser(String userId) {
        Iterable<Acceptance> listAcceptance = acceptanceRepository.findByUserId(userId);
        if(!listAcceptance.iterator().hasNext()){
            throw new NoDataFoundException("There isn't any acceptance for this user to show");
        }
        return listAcceptance;
    }

    private boolean existsByDisclaimerAndUser(String disclaimerId, String userId){
        Optional<Acceptance> acceptance = acceptanceRepository.findByDisclaimerIdAndUserId(disclaimerId,userId);
        if(acceptance.isPresent()){
            return true;
        }
        return false;
    }
}
