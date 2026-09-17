package com.macias34.codemastery.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserResponseDto {
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private List<UserDto> users;
}
