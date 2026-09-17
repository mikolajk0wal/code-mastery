package com.macias34.codemastery.user.dto;

import com.macias34.codemastery.user.entity.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDto {
    private String username;
    private String email;
    private String note;
    private UserRole role;
    private boolean invoiceDetailsSameAsPersonal;
    private UpdatePersonalDetailsDto personalDetails;
    private UpdateInvoiceDetailsDto invoiceDetails;
}
