package com.fivvy.backend.challenge.service;

import com.fivvy.backend.challenge.dto.AcceptanceDTO;
import com.fivvy.backend.challenge.dto.ResponseDTO;
import com.fivvy.backend.challenge.exception.AcceptanceAlreadyExistsException;
import com.fivvy.backend.challenge.exception.DisclaimerNotFoundException;
import com.fivvy.backend.challenge.model.Acceptance;
import com.fivvy.backend.challenge.repository.AcceptanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AcceptanceService extends BaseService{

    @Autowired
    AcceptanceRepository acceptanceRepository;

    @Autowired
    DisclaimerService disclaimerService;

    public ResponseDTO listAcceptance(String userId) {
        if (userId == null) {
            return findAll();
        } else {
            return findByUser(userId);
        }
    }

    private ResponseDTO findAll() {
        Iterable<Acceptance> listAcceptance = acceptanceRepository.findAll();
        if(!listAcceptance.iterator().hasNext()){
            return createErrorResponse("There isn't any acceptance to show");
        }
        return createResponse(listAcceptance);
    }

    private ResponseDTO findByUser(String userId) {
        Iterable<Acceptance> listAcceptance = acceptanceRepository.findByUserId(userId);
        if(!listAcceptance.iterator().hasNext()){
            return createErrorResponse("There isn't any acceptance for this user to show");
        }
        return createResponse(listAcceptance);
    }

    public Acceptance create(AcceptanceDTO dto) throws AcceptanceAlreadyExistsException, DisclaimerNotFoundException {
        Acceptance acceptance = new Acceptance();

        if (existsByDisclaimerAndUser(dto.getDisclaimerId(),dto.getUserId())){
            throw new AcceptanceAlreadyExistsException("Acceptance already exists for that user and disclaimer.");
        }
        if (existsById(dto.getId())){
            throw new AcceptanceAlreadyExistsException("Acceptance already exists for that ID.");
        }
        if (disclaimerService.existsById(dto.getDisclaimerId())){
            acceptance.setUserId(dto.getUserId());
            acceptance.setDisclaimerId(dto.getDisclaimerId());
        }else{
            throw new DisclaimerNotFoundException("Disclaimer ID not found.");
        }

        return acceptanceRepository.save(acceptance);
    }

    private boolean existsByDisclaimerAndUser(String disclaimerId, String userId){
        try{
            Acceptance acceptance = acceptanceRepository.findByDisclaimerIdAndUserId(disclaimerId,userId);
            if(acceptance == null){
                return false;
            }else{
                return true;
            }
        }catch (IllegalArgumentException e){
            return false;
        }
    }
    private boolean existsById(String id){
        boolean result;
        try{
            result = acceptanceRepository.existsById(id);
            return result;
        }catch (IllegalArgumentException e){
            return false;
        }
    }
}
