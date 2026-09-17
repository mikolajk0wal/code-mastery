package com.macias34.codemastery.user.dto;

import com.macias34.codemastery.course.dto.course.CourseDto;
import com.macias34.codemastery.order.entity.OrderEntity;
import com.macias34.codemastery.user.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private int id;
    private String username;
    private String email;
    private String note;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private UserRole role;
    private PersonalDetailsDto personalDetails;
    private InvoiceDetailsDto invoiceDetails;
    private Set<CourseDto> courses = new HashSet<>();
    // private List<OrderEntity> orders = new ArrayList<>(); // TODO order dto
}
