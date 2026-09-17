package com.macias34.codemastery.course.dto.course;

import com.macias34.codemastery.course.dto.category.CategoryDto;
import com.macias34.codemastery.course.dto.property.PropertyDto;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
public class UsersCourseDto {
    private int id;
    private String name;
    private double price;
    private String instructorName;
    private int participantsCount;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String description;
    private Set<CategoryDto> categories;
    private Set<PropertyDto> properties;
}
