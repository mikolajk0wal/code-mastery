package com.macias34.codemastery.course.dto.lesson;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateLessonDto {
    @NotNull
    private String title;
    @NotNull
    private int chapterId;
}
