package com.macias34.codemastery.course.dto.property;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdatePropertyDto {
    @NotBlank
    private String label;
    @NotBlank
    private String value;
}
