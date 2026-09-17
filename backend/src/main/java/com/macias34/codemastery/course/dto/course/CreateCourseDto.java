package com.macias34.codemastery.course.dto.course;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class CreateCourseDto {
    @NotNull
    private String name;
    @NotNull
    @Min(0)
    private Double price;
    @NotNull
    private String instructorName;
    @NotNull
    private String description;

    @NotNull
    private Set<Integer> categoriesIds;
}
