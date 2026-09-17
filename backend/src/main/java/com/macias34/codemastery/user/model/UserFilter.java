package com.macias34.codemastery.user.model;

import com.macias34.codemastery.user.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class UserFilter {
    private String username;
    private String email;
    private UserRole role;
}
