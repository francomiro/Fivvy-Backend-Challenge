package com.fivvy.backend.challenge.service;

import com.fivvy.backend.challenge.dto.DisclaimerDTO;
import com.fivvy.backend.challenge.dto.ResponseDTO;
import com.fivvy.backend.challenge.exception.DisclaimerAlreadyExistsException;
import com.fivvy.backend.challenge.exception.DisclaimerNotFoundException;
import com.fivvy.backend.challenge.model.Disclaimer;
import com.fivvy.backend.challenge.repository.DisclaimerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class DisclaimerService extends BaseService {

    @Autowired
    DisclaimerRepository disclaimerRepository;


    public ResponseDTO listDisclaimer(String text) {
        if (text == null) {
            return findAll();
        } else {
            return findByText(text);
        }
    }

    public Disclaimer create(DisclaimerDTO disclaimerDTO) throws DisclaimerAlreadyExistsException {
        if (existsById(disclaimerDTO.getId())){
            throw new DisclaimerAlreadyExistsException("Disclaimer ID already exists.");
        }
        Disclaimer disclaimer = new Disclaimer();
        disclaimer.setName(disclaimerDTO.getName());
        disclaimer.setText(disclaimerDTO.getText());
        disclaimer.setVersion(disclaimerDTO.getVersion());


        return disclaimerRepository.save(disclaimer);
    }

    public Disclaimer update(DisclaimerDTO disclaimerDTO) throws DisclaimerNotFoundException {
        if(!existsById(disclaimerDTO.getId())){
            throw new DisclaimerNotFoundException("Disclaimer ID not found");
        }
        Disclaimer disclaimer = findById(disclaimerDTO.getId()).get();
        disclaimer.setName(disclaimerDTO.getName() != null ? disclaimerDTO.getName() : disclaimer.getName());
        disclaimer.setText(disclaimerDTO.getText() != null ? disclaimerDTO.getText() : disclaimer.getText());
        disclaimer.setVersion(disclaimerDTO.getVersion() != null ? disclaimerDTO.getVersion() : disclaimer.getVersion());
        disclaimer.setUpdateAt(new Date());

        return disclaimerRepository.save(disclaimer);
    }

    public void delete(String id) throws DisclaimerNotFoundException {
        if(!existsById(id)){
            throw new DisclaimerNotFoundException("Disclaimer ID not found");
        }
        disclaimerRepository.deleteById(id);
    }

    public boolean existsById(String id){
        boolean result;
        try{
            result = disclaimerRepository.existsById(id);
            return result;
        }catch (IllegalArgumentException e){
            return false;
        }
    }

    private Optional<Disclaimer> findById(String id){
        return disclaimerRepository.findById(id);
    }

    private ResponseDTO findByText(String text) {
        Iterable<Disclaimer> listDisclaimers = disclaimerRepository.findAllByTextContaining(text);
        if(!listDisclaimers.iterator().hasNext()){
            return createErrorResponse("There isn't any disclaimer to show with that text");
        }
        return createResponse(listDisclaimers);
    }

    private ResponseDTO findAll(){
        Iterable<Disclaimer> listDisclaimers = disclaimerRepository.findAll();

        if(!listDisclaimers.iterator().hasNext()){
            return createErrorResponse("There isn't any disclaimer to show");
        }
        return createResponse(listDisclaimers);
    }

}
