package com.fivvy.backend.challenge.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisclaimerDTO {

    private String id;

    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String text;
    @NotNull
    @NotBlank
    private String version;
}
