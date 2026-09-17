package com.macias34.codemastery.user.mapper;

import com.macias34.codemastery.user.dto.PersonalDetailsDto;
import com.macias34.codemastery.user.entity.PersonalDetailsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface PersonalDetailsMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    PersonalDetailsEntity fromDtoToEntity(PersonalDetailsDto dto);
}
