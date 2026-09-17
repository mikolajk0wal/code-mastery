package com.macias34.codemastery.course.mapper;

import com.macias34.codemastery.course.dto.property.PropertyDto;
import com.macias34.codemastery.course.entity.PropertyEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PropertyMapper {
    PropertyDto fromEntityToDto(PropertyEntity property);
}
