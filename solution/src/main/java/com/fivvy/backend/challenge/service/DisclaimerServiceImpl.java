package com.fivvy.backend.challenge.service;

import com.fivvy.backend.challenge.dto.DisclaimerDTO;
import com.fivvy.backend.challenge.exception.DisclaimerAlreadyExistsException;
import com.fivvy.backend.challenge.exception.DisclaimerNotFoundException;
import com.fivvy.backend.challenge.exception.MissingFieldsException;
import com.fivvy.backend.challenge.exception.NoDataFoundException;
import com.fivvy.backend.challenge.model.Disclaimer;
import com.fivvy.backend.challenge.repository.DisclaimerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Date;
import java.util.Optional;

@Service
public class DisclaimerServiceImpl implements DisclaimerService {

    @Autowired
    DisclaimerRepository disclaimerRepository;


    @Override
    public Iterable<Disclaimer> listDisclaimer(Optional<String> text) {
        Iterable<Disclaimer> disclaimers;
        if (!text.isPresent()) {
            disclaimers = findAll();
        } else {
            disclaimers = findByText(text.get());
        }
        return disclaimers;
    }

    @Override
    public Disclaimer create(DisclaimerDTO disclaimerDTO){
        Disclaimer disclaimer = new Disclaimer();
        disclaimer.setName(disclaimerDTO.getName());
        disclaimer.setText(disclaimerDTO.getText());
        disclaimer.setVersion(disclaimerDTO.getVersion());

        return disclaimerRepository.save(disclaimer);
    }

    @Override
    public Disclaimer update(DisclaimerDTO disclaimerDTO){
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

    @Override
    public void delete(String id){
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

    private Iterable<Disclaimer> findByText(String text) {
        Iterable<Disclaimer> listDisclaimers = disclaimerRepository.findAllByTextContaining(text);
        if(!listDisclaimers.iterator().hasNext()){
            throw new NoDataFoundException("There isn't any disclaimer to show with that text");
        }
        return listDisclaimers;
    }

    private Iterable<Disclaimer> findAll(){
        Iterable<Disclaimer> listDisclaimers = disclaimerRepository.findAll();
        if(!listDisclaimers.iterator().hasNext()){
            throw new NoDataFoundException("There isn't any disclaimer to show");
        }
        return listDisclaimers;
    }

    @Override
    public void checkConstraint(DisclaimerDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            throw new MissingFieldsException("Check the fields, they must not be empty or null");
        }
        if (existsById(dto.getId())){
            throw new DisclaimerAlreadyExistsException("Disclaimer ID already exists.");
        }
    }

}
