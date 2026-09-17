package com.macias34.codemastery.auth.dto;

import com.macias34.codemastery.user.dto.InvoiceDetailsDto;
import com.macias34.codemastery.user.dto.PersonalDetailsDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SignUpDto {
	@NotNull(message = "Username cannot be empty")
	@Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
	private String username;

	@NotNull(message = "Password cannot be empty")
	@Size(min = 5, message = "Password must be at least 5 characters long")
	private String password;

	@NotNull(message = "Email cannot be empty")
	@Email(message = "Email should be valid")
	private String email;


	@NotNull
	private PersonalDetailsDto personalDetails;

	private boolean invoiceDetailsSameAsPersonal;

	@NotNull
	private InvoiceDetailsDto invoiceDetailsDto;
}
