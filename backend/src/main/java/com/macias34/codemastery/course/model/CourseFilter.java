package com.macias34.codemastery.course.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CourseFilter {
    private String name;
    private double minPrice;
    private double maxPrice;
    private int minParticipantsCount;
    private int categoryId;
}
