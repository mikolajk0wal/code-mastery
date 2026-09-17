package com.macias34.codemastery.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PersonalDetailsDto {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String postalCode;
    @NotNull
    private String city;
    @NotNull
    private String street;
    @NotNull
    private String phoneNumber;
}
