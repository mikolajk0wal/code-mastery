package com.macias34.codemastery.informationPage.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateInformationPageDto {
    @Nullable
    private String title;
    @Nullable
    private String content;
}
