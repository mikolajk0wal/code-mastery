package com.macias34.codemastery.user.dto;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePersonalDetailsDto {
    @Nullable
    private String firstName;
    @Nullable
    private String lastName;
    @Nullable
    private String postalCode;
    @Nullable
    private String city;
    @Nullable
    private String street;
    @Nullable
    private String phoneNumber;
}
