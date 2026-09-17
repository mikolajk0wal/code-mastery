package com.macias34.codemastery.informationPage.mapper;

import com.macias34.codemastery.informationPage.dto.InformationPageDto;
import com.macias34.codemastery.informationPage.entity.InformationPageEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface InformationPageMapper {
    InformationPageDto fromEntityToDto(InformationPageEntity informationPage);
}
