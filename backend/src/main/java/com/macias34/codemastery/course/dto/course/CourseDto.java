package com.macias34.codemastery.course.dto.course;

import com.macias34.codemastery.course.dto.category.CategoryDto;
import com.macias34.codemastery.course.dto.chapter.ChapterDto;
import com.macias34.codemastery.course.dto.property.PropertyDto;
import com.macias34.codemastery.course.entity.CategoryEntity;
import com.macias34.codemastery.course.entity.ChapterEntity;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class CourseDto {
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
    private List<ChapterDto> chapters;
    private String thumbnailSrc;
}
