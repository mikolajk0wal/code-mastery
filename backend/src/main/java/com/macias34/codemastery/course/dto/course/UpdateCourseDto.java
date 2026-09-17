package com.macias34.codemastery.course.dto.course;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class UpdateCourseDto {
    @Nullable
    @Size(min = 3, max = 50)
    private String name;
    @Min(0)
    @Nullable
    private Double price;
    @Nullable
    @Size(min = 3, max = 50)
    private String instructorName;
    @Nullable
    @Size(min = 3, max = 2000)
    private String description;
    @Nullable
    private Set<Integer> categoriesIds;
    @Nullable
    @Size(min = 3)
    private String thumbnailSrc;
}
