package com.macias34.codemastery.course.dto.lesson;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class LessonDto {
    private int id;
    private String title;
    private int chapterId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String videoSrc;
}
