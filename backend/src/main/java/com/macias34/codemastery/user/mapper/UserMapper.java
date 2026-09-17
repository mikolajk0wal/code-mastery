package com.macias34.codemastery.user.mapper;

import com.macias34.codemastery.course.mapper.CourseMapper;
import com.macias34.codemastery.user.dto.UserDto;
import com.macias34.codemastery.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {CourseMapper.class, PersonalDetailsMapper.class, InvoiceDetailsMapper.class})

public interface UserMapper {
    @Mapping(target = "personalDetails", source = "personalDetails")
    @Mapping(target = "invoiceDetails", source = "invoiceDetails")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    UserDto fromEntityToDto(UserEntity user);
}
