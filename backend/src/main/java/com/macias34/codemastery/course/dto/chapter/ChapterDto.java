package com.macias34.codemastery.course.dto.chapter;

import com.macias34.codemastery.course.dto.lesson.LessonDto;
import com.macias34.codemastery.course.entity.CourseEntity;
import com.macias34.codemastery.course.entity.LessonEntity;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
public class ChapterDto {
    private int id;
    private String title;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<LessonDto> lessons;

    private int courseId;
}
