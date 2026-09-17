package com.macias34.codemastery.course.mapper;

import com.macias34.codemastery.course.entity.ChapterEntity;
import com.macias34.codemastery.course.dto.chapter.ChapterDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = LessonMapper.class)
public interface ChapterMapper {
    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    ChapterDto fromEntityToDto(ChapterEntity chapterEntity);
}
