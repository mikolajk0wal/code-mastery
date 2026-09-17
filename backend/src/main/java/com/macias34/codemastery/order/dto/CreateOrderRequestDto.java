package com.macias34.codemastery.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderRequestDto {
    private Integer courseId;
    private Integer userId;
}
