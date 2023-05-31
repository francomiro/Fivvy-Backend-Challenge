package com.fivvy.backend.challenge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fivvy.backend.challenge.controller.DisclaimerController;
import com.fivvy.backend.challenge.dto.DisclaimerDTO;
import com.fivvy.backend.challenge.exception.DisclaimerNotFoundException;
import com.fivvy.backend.challenge.exception.MissingFieldsException;
import com.fivvy.backend.challenge.exception.NoDataFoundException;
import com.fivvy.backend.challenge.model.Disclaimer;
import com.fivvy.backend.challenge.service.DisclaimerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DisclaimerController.class)
public class DisclaimerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private DisclaimerService disclaimerService;


//    TESTS listDisclaimer - GET
    @Test
    void getAllDisclaimers_WhenThereIsNoDataAndParamTextIsNull_ReturnAMessage() throws Exception {

        when(disclaimerService.listDisclaimer(java.util.Optional.empty())).thenThrow(new NoDataFoundException("There isn't any disclaimer to show"));

        mockMvc.perform(get("/disclaimer/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.body").doesNotExist())
                .andExpect(jsonPath("$.error.message").value("There isn't any disclaimer to show"));
    }

    @Test
    void getAllDisclaimers_WhenThereIsDataAndParamTextIsNull_ReturnListOfDisclaimers() throws Exception {

        Disclaimer disclaimer1 = new Disclaimer("Terms and Conditions","do you accept the conditions?","1.0.0");
        Disclaimer disclaimer2 = new Disclaimer("Terms of Contract","do you accept this terms of the contract?","3.2.1");
        List<Disclaimer> listDisclaimers = new ArrayList<>();
        listDisclaimers.add(disclaimer1);
        listDisclaimers.add(disclaimer2);

        when(disclaimerService.listDisclaimer(java.util.Optional.empty())).thenReturn(listDisclaimers);
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

        when(disclaimerService.listDisclaimer(Optional.of("Term"))).thenThrow( new NoDataFoundException("There isn't any disclaimer to show with that text"));

        mockMvc.perform(get("/disclaimer/")
                        .param("text", "Term")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.body").doesNotExist())
                .andExpect(jsonPath("$.error.message").value("There isn't any disclaimer to show with that text"));
    }

    @Test
    void getAllDisclaimers_WhenThereIsDataWithParamTextInRequest_ReturnListOfDisclaimers() throws Exception {
        Disclaimer disclaimer2 = new Disclaimer("Terms of Contract","do you accept this terms of the contract?","3.2.1");
        Disclaimer disclaimer3 = new Disclaimer("Contract final","This is the final contract!","2.2.1");

        List<Disclaimer> listDisclaimers = new ArrayList<>();
        listDisclaimers.add(disclaimer2);
        listDisclaimers.add(disclaimer3);

        when(disclaimerService.listDisclaimer(Optional.of("contract"))).thenReturn(listDisclaimers);

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

        when(disclaimerService.create(disclaimerDTO)).thenThrow(new MissingFieldsException("Check the fields, they must not be empty or null"));

        mockMvc.perform(post("/disclaimer/")
                        .content(objectMapper.writeValueAsString(disclaimerDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value("Check the fields, they must not be empty or null"))
                .andExpect(jsonPath("$.body").doesNotExist());
    }

    @Test
    void postCreateDisclaimer_WhenTheRequestIsComplete_ReturnMessage() throws Exception {
        DisclaimerDTO disclaimerDTO = new DisclaimerDTO();
        disclaimerDTO.setName("Contract final");
        disclaimerDTO.setVersion("2.2.1");
        disclaimerDTO.setText("This is the final contract!");

        Disclaimer disclaimer = new Disclaimer(disclaimerDTO.getName(),disclaimerDTO.getText(),disclaimerDTO.getVersion());

        when(disclaimerService.create(disclaimerDTO)).thenReturn(disclaimer);

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

        mockMvc.perform(put("/disclaimer/")
                        .content(objectMapper.writeValueAsString(disclaimerDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.message").value("Disclaimer ID not found"))
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

        mockMvc.perform(put("/disclaimer/")
                        .content(objectMapper.writeValueAsString(disclaimerDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").doesNotExist())
                .andExpect(jsonPath("$.body").value("Disclamer updated successfully."));
    }

    //TEST DELETE
    @Test
    void putDeleteDisclaimer_whenIdIsOK_returnMessage() throws Exception {
        String disclaimerId = "123";
        mockMvc.perform(delete("/disclaimer/{disclaimerId}", disclaimerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").doesNotExist())
                .andExpect(jsonPath("$.body").value("Disclamer deleted successfully."));
    }

}
