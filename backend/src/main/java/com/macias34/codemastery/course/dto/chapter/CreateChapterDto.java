package com.macias34.codemastery.course.dto.chapter;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateChapterDto {
    @NotNull
    private String title;
    @NotNull
    private int courseId;
}
