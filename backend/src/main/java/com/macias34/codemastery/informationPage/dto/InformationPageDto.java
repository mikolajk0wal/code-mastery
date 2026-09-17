package com.macias34.codemastery.informationPage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InformationPageDto {
    private int id;
    private String title;
    private String content;
    private String slug;
}
