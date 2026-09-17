package com.macias34.codemastery.informationPage.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateInformationPageDto {
    @NotNull
    private String title;
    @NotNull
    private String content;
}
