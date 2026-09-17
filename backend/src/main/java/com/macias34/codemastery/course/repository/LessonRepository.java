package com.macias34.codemastery.course.repository;

import com.macias34.codemastery.course.entity.LessonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<LessonEntity,Integer> {
    List<LessonEntity> findLessonEntitiesByChapter_Id(int chapterId);
}
