package com.macias34.codemastery.user.mapper;

import com.macias34.codemastery.course.mapper.CourseMapper;
import com.macias34.codemastery.user.dto.InvoiceDetailsDto;
import com.macias34.codemastery.user.entity.InvoiceDetailsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InvoiceDetailsMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    InvoiceDetailsEntity fromDtoToEntity(InvoiceDetailsDto dto);
}
