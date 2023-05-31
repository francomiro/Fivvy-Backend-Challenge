package com.fivvy.backend.challenge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fivvy.backend.challenge.controller.AcceptanceController;
import com.fivvy.backend.challenge.dto.AcceptanceDTO;
import com.fivvy.backend.challenge.exception.AcceptanceAlreadyExistsException;
import com.fivvy.backend.challenge.exception.DisclaimerNotFoundException;
import com.fivvy.backend.challenge.exception.MissingFieldsException;
import com.fivvy.backend.challenge.exception.NoDataFoundException;
import com.fivvy.backend.challenge.model.Acceptance;
import com.fivvy.backend.challenge.service.AcceptanceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AcceptanceController.class)
public class AcceptanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private AcceptanceService acceptanceService;

    //TESTS listAcceptance - GET
    @Test
    void getAllAcceptance_WhenThereIsNoDataAndParamUserIdIsNull_ReturnAMessage() throws Exception {

        when(acceptanceService.listAcceptance(any(Optional.class))).thenThrow(new NoDataFoundException("There isn't any acceptance to show"));

        mockMvc.perform(get("/acceptance/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.body").doesNotExist())
                .andExpect(jsonPath("$.error.message").value("There isn't any acceptance to show"));

    }

    @Test
    void getAllAcceptances_WhenThereIsDataAndParamUserIdIsNull_ReturnListOfAllAcceptances() throws Exception {

        Acceptance acceptance1 = new Acceptance("8dd6-8b7629c617078","fmiro");
        Acceptance acceptance2 = new Acceptance("324b2794-fe66-4859","mtorres");

        List<Acceptance> listAcceptance = new ArrayList<>();
        listAcceptance.add(acceptance1);
        listAcceptance.add(acceptance2);

        when(acceptanceService.listAcceptance(any(Optional.class))).thenReturn(listAcceptance);
        mockMvc.perform(get("/acceptance/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body", hasSize(2)))
                .andExpect(jsonPath("$.body[*].disclaimerId").value(containsInAnyOrder("8dd6-8b7629c617078", "324b2794-fe66-4859")))
                .andExpect(jsonPath("$.body[*].userId").value(containsInAnyOrder("fmiro", "mtorres")))
                .andExpect(jsonPath("$.error").doesNotExist());
    }

    @Test
    void getAllAcceptance_WhenThereIsNoDataWithParamUserIdInRequest_ReturnAMessage() throws Exception {
        when(acceptanceService.listAcceptance(Optional.of("fmiro"))).thenThrow(new NoDataFoundException("There isn't any acceptance for this user to show"));

        mockMvc.perform(get("/acceptance/")
                        .param("userId", "fmiro")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.body").doesNotExist())
                .andExpect(jsonPath("$.error.message").value("There isn't any acceptance for this user to show"));
    }

    @Test
    void getAllAcceptance_WhenThereIsDataWithParamUserIdInRequest_ReturnListOfAcceptances() throws Exception {
        Acceptance acceptance1 = new Acceptance("8dd6-8b7629c617078","fmiro");
        Acceptance acceptance2 = new Acceptance("324b2794-fe66-4859","fmiro");

        List<Acceptance> listAcceptance = new ArrayList<>();
        listAcceptance.add(acceptance1);
        listAcceptance.add(acceptance2);

        when(acceptanceService.listAcceptance(Optional.of("fmiro"))).thenReturn(listAcceptance);

        mockMvc.perform(get("/acceptance/")
                        .param("userId", "fmiro")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body", hasSize(2)))
                .andExpect(jsonPath("$.body[*].disclaimerId").value(containsInAnyOrder("8dd6-8b7629c617078", "324b2794-fe66-4859")))
                .andExpect(jsonPath("$.body[*].userId").value(containsInAnyOrder("fmiro", "fmiro")))
                .andExpect(jsonPath("$.error").doesNotExist());
    }

    //TESTS createAcceptance - POST

    @Test
    void postCreateAcceptance_WhenTheRequestHaveUserIdNull_ReturnMessage() throws Exception {
        AcceptanceDTO acceptanceDTO = new AcceptanceDTO();
         acceptanceDTO.setDisclaimerId("8dd6-8b7629c617078");

        when(acceptanceService.create(acceptanceDTO)).thenThrow(new MissingFieldsException("Check the fields, they must not be empty or null"));

        mockMvc.perform(post("/acceptance/")
                        .content(objectMapper.writeValueAsString(acceptanceDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value("Check the fields, they must not be empty or null"));

    }

    @Test
    void postCreateAcceptance_WhenTheRequestIsComplete_ReturnMessage() throws Exception {
        AcceptanceDTO acceptanceDTO = new AcceptanceDTO();
        acceptanceDTO.setDisclaimerId("8dd6-8b7629c617078");
        acceptanceDTO.setUserId("fmiro");


        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        Acceptance acceptance = new Acceptance(acceptanceDTO.getDisclaimerId(),acceptanceDTO.getUserId());
        when(acceptanceService.create(acceptanceDTO)).thenReturn(acceptance);

        mockMvc.perform(post("/acceptance/")
                        .content(objectMapper.writeValueAsString(acceptanceDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.error").doesNotExist())
                .andExpect(jsonPath("$.body").value("Acceptance created successfully."));
    }

    @Test
    void postCreateAcceptance_WhenAcceptanceNotExist_ReturnMessage() throws Exception {
        AcceptanceDTO acceptanceDTO = new AcceptanceDTO();
        acceptanceDTO.setDisclaimerId("wrongDisclaimerID");
        acceptanceDTO.setUserId("fmiro");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        when(acceptanceService.create(acceptanceDTO)).thenThrow(new DisclaimerNotFoundException("Disclaimer ID not found."));

        mockMvc.perform(post("/acceptance/")
                        .content(objectMapper.writeValueAsString(acceptanceDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.message").value("Disclaimer ID not found."))
                .andExpect(jsonPath("$.body").doesNotExist());
    }

    @Test
    void postCreateAcceptance_WhenAcceptanceForThisUserAndDisclaimerIsAlreadyCreated_ReturnMessage() throws Exception {
        AcceptanceDTO acceptanceDTO = new AcceptanceDTO();
        acceptanceDTO.setDisclaimerId("8dd6-8b7629c617078");
        acceptanceDTO.setUserId("fmiro");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        when(acceptanceService.create(acceptanceDTO)).thenThrow(new AcceptanceAlreadyExistsException("Acceptance already exists for that user and disclaimer."));

        mockMvc.perform(post("/acceptance/")
                        .content(objectMapper.writeValueAsString(acceptanceDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value("Acceptance already exists for that user and disclaimer."))
                .andExpect(jsonPath("$.body").doesNotExist());
    }


}
