package com.macias34.codemastery.course.dto.property;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreatePropertyDto {
    @NotNull
    private String label;
    @NotNull
    private String value;
    @NotNull
    private int courseId;
}
