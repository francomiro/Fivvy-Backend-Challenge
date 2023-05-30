package com.fivvy.backend.challenge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fivvy.backend.challenge.controller.DisclaimerController;
import com.fivvy.backend.challenge.dto.DisclaimerDTO;
import com.fivvy.backend.challenge.dto.ResponseDTO;
import com.fivvy.backend.challenge.exception.DisclaimerNotFoundException;
import com.fivvy.backend.challenge.model.Disclaimer;
import com.fivvy.backend.challenge.service.DisclaimerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(DisclaimerController.class)
public class DisclaimerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private DisclaimerService disclaimerService;


    //TESTS listDisclaimer - GET
    @Test
    void getAllDisclaimers_WhenThereIsNoDataAndParamTextIsNull_ReturnAMessage() throws Exception {
        ResponseDTO response = new ResponseDTO<>();
        response.setError("There isn't any disclaimer to show");

        when(disclaimerService.listDisclaimer(null)).thenReturn(response);

        mockMvc.perform(get("/disclaimer/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").doesNotExist())
                .andExpect(jsonPath("$.error").value("There isn't any disclaimer to show"));
    }

    @Test
    void getAllDisclaimers_WhenThereIsDataAndParamTextIsNull_ReturnListOfDisclaimers() throws Exception {

        Disclaimer disclaimer1 = new Disclaimer("Terms and Conditions","do you accept the conditions?","1.0.0");
        Disclaimer disclaimer2 = new Disclaimer("Terms of Contract","do you accept this terms of the contract?","3.2.1");
        List<Disclaimer> listDisclaimers = new ArrayList<>();
        listDisclaimers.add(disclaimer1);
        listDisclaimers.add(disclaimer2);

        ResponseDTO response = new ResponseDTO<>();
        response.setBody(listDisclaimers);

        when(disclaimerService.listDisclaimer(null)).thenReturn(response);
        mockMvc.perform(get("/disclaimer/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body", hasSize(2)))
                .andExpect(jsonPath("$.body[*].name").value(containsInAnyOrder("Terms and Conditions", "Terms of Contract")))
                .andExpect(jsonPath("$.body[*].text").value(containsInAnyOrder("do you accept the conditions?", "do you accept this terms of the contract?")))
                .andExpect(jsonPath("$.body[*].version").value(containsInAnyOrder("1.0.0", "3.2.1")))
                .andExpect(jsonPath("$.error").doesNotExist());
    }

    @Test
    void getAllDisclaimers_WhenThereIsNoDataWithParamTextInRequest_ReturnAMessage() throws Exception {
        ResponseDTO response = new ResponseDTO<>();
        response.setError("There isn't any disclaimer to show with that text");

        when(disclaimerService.listDisclaimer("Term")).thenReturn(response);

        mockMvc.perform(get("/disclaimer/")
                        .param("text", "Term")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").doesNotExist())
                .andExpect(jsonPath("$.error").value("There isn't any disclaimer to show with that text"));
    }

    @Test
    void getAllDisclaimers_WhenThereIsDataWithParamTextInRequest_ReturnListOfDisclaimers() throws Exception {
        Disclaimer disclaimer2 = new Disclaimer("Terms of Contract","do you accept this terms of the contract?","3.2.1");
        Disclaimer disclaimer3 = new Disclaimer("Contract final","This is the final contract!","2.2.1");

        List<Disclaimer> listDisclaimers = new ArrayList<>();
        listDisclaimers.add(disclaimer2);
        listDisclaimers.add(disclaimer3);

        ResponseDTO response = new ResponseDTO<>();
        response.setBody(listDisclaimers);

        when(disclaimerService.listDisclaimer("contract")).thenReturn(response);

        mockMvc.perform(get("/disclaimer/")
                        .param("text", "contract")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body", hasSize(2)))
                .andExpect(jsonPath("$.body[*].name").value(containsInAnyOrder("Contract final", "Terms of Contract")))
                .andExpect(jsonPath("$.body[*].text").value(containsInAnyOrder("This is the final contract!", "do you accept this terms of the contract?")))
                .andExpect(jsonPath("$.body[*].version").value(containsInAnyOrder("2.2.1", "3.2.1")))
                .andExpect(jsonPath("$.error").doesNotExist());
    }


    //TESTS createDisclaimer - POST

    @Test
    void postCreateDisclaimer_WhenTheRequestHaveNameNull_ReturnMessage() throws Exception {
        DisclaimerDTO disclaimerDTO = new DisclaimerDTO();
        disclaimerDTO.setVersion("2.2.1");
        disclaimerDTO.setText("This is the final contract!");

        ResponseDTO response = new ResponseDTO<>();
        response.setError("BAD REQUEST - Check the fields, they must not be empty or null");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        when(disclaimerService.createErrorResponse(response.getError())).thenReturn(response);

        mockMvc.perform(post("/disclaimer/")
                        .content(objectMapper.writeValueAsString(disclaimerDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("BAD REQUEST - Check the fields, they must not be empty or null"))
                .andExpect(jsonPath("$.body").doesNotExist());
    }

    @Test
    void postCreateDisclaimer_WhenTheRequestIsComplete_ReturnMessage() throws Exception {
        DisclaimerDTO disclaimerDTO = new DisclaimerDTO();
        disclaimerDTO.setName("Contract final");
        disclaimerDTO.setVersion("2.2.1");
        disclaimerDTO.setText("This is the final contract!");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        Disclaimer disclaimer = new Disclaimer(disclaimerDTO.getName(),disclaimerDTO.getText(),disclaimerDTO.getVersion());
        when(disclaimerService.create(disclaimerDTO)).thenReturn(disclaimer);

        ResponseDTO response = new ResponseDTO<>();
        response.setBody("Disclaimer created successfully.");
        when(disclaimerService.createResponse(response.getBody())).thenReturn(response);

        mockMvc.perform(post("/disclaimer/")
                        .content(objectMapper.writeValueAsString(disclaimerDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.error").doesNotExist())
                .andExpect(jsonPath("$.body").value("Disclaimer created successfully."));
    }


    //TESTS updateDisclaimer - PUT
    @Test
    void putUpdateDisclaimer_whenIdIsEmptyOrWrong_returnErrorMessage() throws Exception {
        DisclaimerDTO disclaimerDTO = new DisclaimerDTO();
        disclaimerDTO.setVersion("2.2.1");
        disclaimerDTO.setText("This is the final contract!");

        when(disclaimerService.update(disclaimerDTO))
                .thenThrow(new DisclaimerNotFoundException("Disclaimer ID not found"));

        ResponseDTO response = new ResponseDTO<>();
        response.setError("Disclaimer ID not found");
        when(disclaimerService.createErrorResponse(response.getError())).thenReturn(response);

        mockMvc.perform(put("/disclaimer/")
                        .content(objectMapper.writeValueAsString(disclaimerDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Disclaimer ID not found"))
                .andExpect(jsonPath("$.body").doesNotExist());
    }

    @Test
    void putUpdateDisclaimer_whenIdIsOK_returnMessage() throws Exception {
        DisclaimerDTO disclaimerDTO = new DisclaimerDTO();
        disclaimerDTO.setId("324b2794-fe66-4859-8dd6-8b7629c61707");
        disclaimerDTO.setVersion("2.2.1");
        disclaimerDTO.setText("This is the final contract!");

        Disclaimer disclaimer = new Disclaimer();
        disclaimer.setId(disclaimerDTO.getId());
        disclaimer.setVersion(disclaimerDTO.getVersion());
        disclaimer.setText(disclaimerDTO.getText());

        when(disclaimerService.update(disclaimerDTO))
                .thenReturn(disclaimer);

        ResponseDTO response = new ResponseDTO<>();
        response.setBody("Disclamer updated successfully.");
        when(disclaimerService.createResponse(response.getBody())).thenReturn(response);

        mockMvc.perform(put("/disclaimer/")
                        .content(objectMapper.writeValueAsString(disclaimerDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").doesNotExist())
                .andExpect(jsonPath("$.body").value("Disclamer updated successfully."));
    }


}
