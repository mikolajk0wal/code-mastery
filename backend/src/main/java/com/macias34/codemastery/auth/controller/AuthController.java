package com.macias34.codemastery.auth.controller;

import com.macias34.codemastery.user.dto.UserDto;
import com.macias34.codemastery.user.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.macias34.codemastery.auth.dto.SignInResponse;
import com.macias34.codemastery.auth.dto.SignInDto;
import com.macias34.codemastery.auth.dto.SignUpDto;
import com.macias34.codemastery.auth.service.AuthService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Tag(name = "auth")
@RestController
@RequestMapping("/auth")
public class AuthController {

	private AuthService authService;
	private UserService userService;

	@PostMapping("/sign-up")
	public ResponseEntity<String> signUp(@RequestBody SignUpDto signUpDto) throws MessagingException {
		authService.createUser(signUpDto);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/sign-in")
	public ResponseEntity<SignInResponse> signIn(@RequestBody SignInDto signInDto) {
		userService.checkIfUserDoesntExist(signInDto);
		userService.checkIfUserHasConfirmedEmail(signInDto);

		String token = authService.authenticateAndGenerateJwt(signInDto);

		return new ResponseEntity<>(new SignInResponse(token), HttpStatus.OK);
	}
}
