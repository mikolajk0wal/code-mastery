package com.macias34.codemastery.order.mapper;

import com.macias34.codemastery.course.mapper.CourseMapper;
import com.macias34.codemastery.order.dto.OrderDto;
import com.macias34.codemastery.order.entity.OrderEntity;
import com.macias34.codemastery.user.mapper.UserMapper;
import org.mapstruct.Mapper;

@Mapper(uses = {CourseMapper.class, UserMapper.class}, componentModel = "spring")
public interface OrderMapper {
    OrderDto mapEntityToDto(OrderEntity entity);
}
