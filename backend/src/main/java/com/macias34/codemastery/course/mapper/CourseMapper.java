package com.macias34.codemastery.course.mapper;

import com.macias34.codemastery.course.dto.course.CourseDto;
import com.macias34.codemastery.course.entity.CourseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = { CategoryMapper.class, ChapterMapper.class, PropertyMapper.class }, componentModel = "spring")
public interface CourseMapper {
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "thumbnailSrc", source = "thumbnail.src")
    CourseDto fromEntityToDto(CourseEntity courseEntity);
}