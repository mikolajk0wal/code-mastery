package com.macias34.codemastery.auth.service;

import com.macias34.codemastery.exception.NoPermissionException;
import com.macias34.codemastery.mail.service.MailService;
import com.macias34.codemastery.user.dto.InvoiceDetailsDto;
import com.macias34.codemastery.user.dto.PasswordChangeDto;
import com.macias34.codemastery.user.dto.PersonalDetailsDto;
import com.macias34.codemastery.user.dto.UserDto;
import com.macias34.codemastery.user.entity.ConfirmationToken;
import com.macias34.codemastery.user.entity.InvoiceDetailsEntity;
import com.macias34.codemastery.user.entity.PersonalDetailsEntity;
import com.macias34.codemastery.user.mapper.InvoiceDetailsMapper;
import com.macias34.codemastery.user.mapper.PersonalDetailsMapper;
import com.macias34.codemastery.user.mapper.UserMapper;
import com.macias34.codemastery.user.repository.ConfirmationTokenRepository;
import com.macias34.codemastery.user.repository.InvoiceDetailsRepository;
import com.macias34.codemastery.user.repository.PersonalDetailsRepository;
import com.macias34.codemastery.user.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.macias34.codemastery.auth.dto.SignInDto;
import com.macias34.codemastery.auth.dto.SignUpDto;
import com.macias34.codemastery.exception.ResourceAlreadyExistsException;
import com.macias34.codemastery.exception.ResourceNotFoundException;
import com.macias34.codemastery.exception.WrongCredentialsException;
import com.macias34.codemastery.security.jwt.JwtGenerator;
import com.macias34.codemastery.user.entity.UserEntity;
import com.macias34.codemastery.user.entity.UserRole;
import com.macias34.codemastery.user.repository.UserRepository;
import com.macias34.codemastery.util.DtoValidator;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthService {
	// todo think about moving sign up method to user service
	private static final String WRONG_CREDENTIALS_MESSAGE = "Username %s has a different password.";

	private final  UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;
	private  final AuthenticationManager authenticationManager;
	private final JwtGenerator jwtGenerator;
	private final InvoiceDetailsMapper invoiceDetailsMapper;
	private final PersonalDetailsMapper personalDetailsMapper;
	private final InvoiceDetailsRepository invoiceDetailsRepository;
	private final PersonalDetailsRepository personalDetailsRepository;
	private final  ConfirmationTokenRepository confirmationTokenRepository;

	@Value("${frontend.url}")
	private String frontendUrl;

	private final MailService mailService;
	private final UserService userService;

	private final UserMapper userMapper;

	public String authenticateAndGenerateJwt(SignInDto signInDto) throws BadCredentialsException {
		Authentication authentication = authenticate(signInDto);
		return jwtGenerator.generateToken(authentication);
	}

	@Transactional
	public void createUser(SignUpDto signUpDto) throws MessagingException {

		if (signUpDto.isInvoiceDetailsSameAsPersonal()) {
			PersonalDetailsDto personalDetails = signUpDto.getPersonalDetails();
			DtoValidator.validate(personalDetails);
			InvoiceDetailsDto invoiceDetails = InvoiceDetailsDto.builder()
					.firstName(personalDetails.getFirstName())
					.lastName(personalDetails.getFirstName())
					.postalCode(personalDetails.getPostalCode())
					.street(personalDetails.getStreet())
					.city(personalDetails.getCity())
					.phoneNumber(personalDetails.getPhoneNumber()).build();
			signUpDto.setInvoiceDetailsDto(invoiceDetails);
		}

		DtoValidator.validate(signUpDto);
		// todo checking if user exists only for users with hasEmailConfirmed
		userService.checkIfUserExists(signUpDto);

		UserEntity user = createUserEntity(signUpDto);
		ConfirmationToken confirmationToken = new ConfirmationToken(user.getEmail());

		String tokenConfirmationUrl = frontendUrl + "/api/user/confirm-email?token="
				+ confirmationToken.getConfirmationToken();

		String mailMessage = "Click this link to confirm your email: " + tokenConfirmationUrl;

		// todo create good looking mail layout
		mailService.sendMail("Confirm your email", mailMessage, user.getEmail());

		confirmationTokenRepository.save(confirmationToken);
		personalDetailsRepository.save(user.getPersonalDetails());
		invoiceDetailsRepository.save(user.getInvoiceDetails());
		userRepository.save(user);
	}

	@Transactional
	public UserDto changePassword(int id, PasswordChangeDto dto, String loggedUserUsername) {
		UserEntity user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		UserEntity loggedUser = userRepository.findByUsername(loggedUserUsername)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		if (loggedUser.getId() != user.getId()) {
			throw new NoPermissionException("You cannot change others' user password");
		}

		Authentication authentication = authenticate(
				new SignInDto(
						loggedUserUsername,
						dto.getOldPassword()
				)
		);

		if(!authentication.isAuthenticated()){
			throw new WrongCredentialsException("Wrong credientals provided");
		}

		DtoValidator.validate(dto);

		user.setPassword(passwordEncoder.encode(dto.getNewPassword()));

		userRepository.save(user);

		return userMapper.fromEntityToDto(user);
	}

	private Authentication authenticate(SignInDto signInDto) {
		try {
			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(signInDto.getUsername(), signInDto.getPassword()));
		} catch (BadCredentialsException e) {
			throw new WrongCredentialsException(String.format(WRONG_CREDENTIALS_MESSAGE, signInDto.getUsername()));
		}
	}

	private UserEntity createUserEntity(SignUpDto signUpDto) {
		UserEntity user = new UserEntity();
		user.setUsername(signUpDto.getUsername());
		user.setEmail(signUpDto.getEmail());
		user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
		user.setRole(UserRole.USER);

		InvoiceDetailsDto invoiceDetailsDto = signUpDto.getInvoiceDetailsDto();
		PersonalDetailsDto personalDetailsDto = signUpDto.getPersonalDetails();

		InvoiceDetailsEntity invoiceDetails = invoiceDetailsMapper.fromDtoToEntity(invoiceDetailsDto);
		PersonalDetailsEntity personalDetails = personalDetailsMapper.fromDtoToEntity(personalDetailsDto);

		user.setInvoiceDetails(invoiceDetails);
		user.setPersonalDetails(personalDetails);

		return user;
	}

}
