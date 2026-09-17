package com.macias34.codemastery.course.repository;

import com.macias34.codemastery.course.entity.ChapterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChapterRepository extends JpaRepository<ChapterEntity,Integer> {
    List<ChapterEntity> findChapterEntitiesByCourse_Id(int courseId);
}
