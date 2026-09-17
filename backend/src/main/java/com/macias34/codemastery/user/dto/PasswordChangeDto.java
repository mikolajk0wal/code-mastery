package com.macias34.codemastery.user.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeDto {
    @Size(min = 5)
    private String oldPassword;
    @Size(min = 5)
    private String newPassword;
}
