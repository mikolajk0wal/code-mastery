package com.macias34.codemastery.course.dto.chapter;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateChapterDto {
    @NotNull
    private String title;
}
