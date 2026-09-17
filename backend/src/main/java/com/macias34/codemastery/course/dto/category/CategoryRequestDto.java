package com.macias34.codemastery.course.dto.category;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Create or update category
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CategoryRequestDto {
    @NotNull
    private String name;
}
