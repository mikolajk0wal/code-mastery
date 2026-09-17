package com.macias34.codemastery.order.dto;

import com.macias34.codemastery.course.dto.course.CourseDto;
import com.macias34.codemastery.course.entity.CourseEntity;
import com.macias34.codemastery.order.entity.OrderStatus;
import com.macias34.codemastery.user.dto.UserDto;
import com.macias34.codemastery.user.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;


@Getter
@Setter
public class OrderDto {
    private int id;
    private OrderStatus status;
    private double price;
    private Timestamp createdAt;
    private List<CourseDto> courses;
    private UserDto user;

}
