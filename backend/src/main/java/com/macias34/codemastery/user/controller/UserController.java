package com.macias34.codemastery.user.controller;

import java.io.IOException;
import java.util.List;

import com.macias34.codemastery.auth.service.AuthService;
import com.macias34.codemastery.user.dto.PasswordChangeDto;
import com.macias34.codemastery.user.dto.ResetPasswordDto;
import com.macias34.codemastery.user.dto.UpdateUserDto;
import com.macias34.codemastery.user.dto.UserDto;
import com.macias34.codemastery.user.dto.UserResponseDto;
import com.macias34.codemastery.user.entity.UserRole;
import com.macias34.codemastery.user.model.UserFilter;
import com.macias34.codemastery.user.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "user")
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final AuthService authService;

	@Value("${frontend.url}")
	private String frontendUrl;


	// TODO all needed crud methods
	@GetMapping("")
	public ResponseEntity<UserResponseDto> getUsers(
			@RequestParam(required = false) String username,
			@RequestParam(required = false) String email,
			@RequestParam(required = false) UserRole role,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		UserFilter userFilter = new UserFilter(username, email, role);
		return ResponseEntity.ok(userService.getAllUsers(userFilter, page, size));
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserDto> getUserById(
			@PathVariable int id) {
		return ResponseEntity.ok(userService.getUserById(id));
	}

	@GetMapping("/me")
	public ResponseEntity<UserDto> getCurrentUserDto() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		UserDto userDto = userService.getUserByUsername(authentication.getName());

		return ResponseEntity.ok(userDto);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<UserDto> updateUser(
			@PathVariable int id,
			@RequestBody UpdateUserDto dto) {
		// todo password change enpoint
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String loggedUserUserName = authentication.getName();

		return ResponseEntity.ok(userService.updateUser(id, dto, loggedUserUserName));
	}

	@PatchMapping("/{id}/password")
	public ResponseEntity<UserDto> changePassword(
			@PathVariable int id,
			@RequestBody PasswordChangeDto dto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String loggedUserUsername = authentication.getName();

		return ResponseEntity.ok(authService.changePassword(id, dto, loggedUserUsername));
	}

	@RequestMapping(value = "/confirm-email", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<?> confirmEmail(@RequestParam("token") String confirmationToken,
			HttpServletResponse httpResponse) {
		userService.confirmEmail(confirmationToken);

		try {
			httpResponse.sendRedirect(frontendUrl + "/auth?emailConfirmed");

		} catch (IOException exception) {
			System.out.println(exception);
		}

		return ResponseEntity.ok().build();
	}

	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestParam("token") String confirmationToken,
										   @RequestBody ResetPasswordDto dto) {

		userService.resetPassword(confirmationToken,dto);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/send-reset-password-link")
	public ResponseEntity<?> sendPasswordResetLink(@RequestParam("email") String email) {
		userService.sendPasswordResetLink(email);
		return ResponseEntity.ok().build();
	}

}
