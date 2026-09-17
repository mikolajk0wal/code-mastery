package com.macias34.codemastery.course.dto.course;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CourseResponseDto {
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private List<CourseDto> courses;
}
