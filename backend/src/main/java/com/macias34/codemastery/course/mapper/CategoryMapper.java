package com.macias34.codemastery.course.mapper;

import com.macias34.codemastery.course.entity.CategoryEntity;
import com.macias34.codemastery.course.dto.category.CategoryDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface CategoryMapper {


    CategoryDto fromEntityToDto(CategoryEntity categoryEntity);
}
