package com.macias34.codemastery.user.service;

import com.macias34.codemastery.auth.dto.SignInDto;
import com.macias34.codemastery.auth.dto.SignUpDto;
import com.macias34.codemastery.auth.service.AuthService;
import com.macias34.codemastery.exception.EmailNotConfirmedException;
import com.macias34.codemastery.exception.NoPermissionException;
import com.macias34.codemastery.exception.ResourceAlreadyExistsException;
import com.macias34.codemastery.exception.ResourceNotFoundException;
import com.macias34.codemastery.exception.WrongCredentialsException;
import com.macias34.codemastery.mail.service.MailService;
import com.macias34.codemastery.user.dto.InvoiceDetailsDto;
import com.macias34.codemastery.user.dto.PasswordChangeDto;
import com.macias34.codemastery.user.dto.PersonalDetailsDto;
import com.macias34.codemastery.user.dto.ResetPasswordDto;
import com.macias34.codemastery.user.dto.UpdateInvoiceDetailsDto;
import com.macias34.codemastery.user.dto.UpdatePersonalDetailsDto;
import com.macias34.codemastery.user.dto.UpdateUserDto;
import com.macias34.codemastery.user.dto.UserDto;
import com.macias34.codemastery.user.entity.ConfirmationToken;
import com.macias34.codemastery.user.dto.UserResponseDto;
import com.macias34.codemastery.user.entity.InvoiceDetailsEntity;
import com.macias34.codemastery.user.entity.PersonalDetailsEntity;
import com.macias34.codemastery.user.entity.UserEntity;
import com.macias34.codemastery.user.entity.UserRole;
import com.macias34.codemastery.user.mapper.InvoiceDetailsMapper;
import com.macias34.codemastery.user.mapper.PersonalDetailsMapper;
import com.macias34.codemastery.user.mapper.UserMapper;
import com.macias34.codemastery.user.model.UserFilter;
import com.macias34.codemastery.user.repository.ConfirmationTokenRepository;
import com.macias34.codemastery.user.model.UserSpecification;
import com.macias34.codemastery.user.repository.InvoiceDetailsRepository;
import com.macias34.codemastery.user.repository.PersonalDetailsRepository;
import com.macias34.codemastery.user.repository.UserRepository;
import com.macias34.codemastery.util.DtoValidator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String USER_EXISTS_MESSAGE = "User %s already exists.";
    private static final String EMAIL_EXISTS_MESSAGE = "User with email %s already exists.";
    private static final String USER_DOESNT_EXIST_MESSAGE = "User %s doesn't exist.";
    private static final String CONFIRMATION_TOKEN_DOESNT_EXIST_MESSAGE = "Confirmation token %s doesn't exist.";
    private static final String USER_NOT_CONFIRMED_EMAIL_MESSAGE = "User %s hasn't confirmed email.";

    private final UserRepository userRepository;
    private final InvoiceDetailsRepository invoiceDetailsRepository;
    private final PersonalDetailsRepository personalDetailsRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    private final UserMapper userMapper;
    private final InvoiceDetailsMapper invoiceDetailsMapper;
    private final PersonalDetailsMapper personalDetailsMapper;

    private final PersonalDetailsService personalDetailsService;
    private final InvoiceDetailsService invoiceDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    @Value("${frontend.url}")
    private String frontendUrl;


    public UserResponseDto getAllUsers(UserFilter userFilter, int page, int size) {
        Specification<UserEntity> spec = UserSpecification.withFilters(userFilter);
        Pageable paging = PageRequest.of(page, size, Sort.by("username"));
        Page<UserEntity> usersPage = userRepository.findAll(spec, paging);

        List<UserDto> userDtos = usersPage.stream().map(userMapper::fromEntityToDto).toList();

        return UserResponseDto.builder()
                .users(userDtos)
                .totalElements(usersPage.getTotalElements())
                .totalPages(usersPage.getTotalPages())
                .build();
    }

    public UserDto getUserById(int id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.fromEntityToDto(user);
    }

    public UserDto getUserByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.fromEntityToDto(user);
    }

    @Transactional
    public UserDto updateUser(int id, UpdateUserDto dto, String loggedUserUsername) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        UserEntity loggedUser = userRepository.findByUsername(loggedUserUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (loggedUser.getId() != user.getId() && loggedUser.getRole() != UserRole.ADMIN) {
            throw new NoPermissionException("You cannot update others' user data");
        }

        if (dto.getPersonalDetails() != null) {
            UpdatePersonalDetailsDto personalDetailsDto = dto.getPersonalDetails();
            DtoValidator.validate(personalDetailsDto);

            PersonalDetailsEntity personalDetails = personalDetailsService
                    .updatePersonalDetails(user.getPersonalDetails().getId(), personalDetailsDto);
            user.setPersonalDetails(personalDetails);

            // TODO Rethink should update invoiceDetails when
            // invoiceDetailsSameAsPersonal=true even if personalDetails are empty (getting
            // previuos val)

            if (dto.isInvoiceDetailsSameAsPersonal()) {
                UpdateInvoiceDetailsDto invoiceDetailsDto = UpdateInvoiceDetailsDto.builder()
                        .firstName(personalDetails.getFirstName())
                        .lastName(personalDetails.getLastName())
                        .postalCode(personalDetails.getPostalCode())
                        .street(personalDetails.getStreet())
                        .city(personalDetails.getCity())
                        .phoneNumber(personalDetails.getPhoneNumber()).build();

                InvoiceDetailsEntity invoiceDetails = invoiceDetailsService
                        .updateInvoiceDetails(user.getInvoiceDetails().getId(), invoiceDetailsDto);
                user.setInvoiceDetails(invoiceDetails);
            }
        }

        if (dto.getInvoiceDetails() != null) {
            UpdateInvoiceDetailsDto invoiceDetailsDto = dto.getInvoiceDetails();
            DtoValidator.validate(invoiceDetailsDto);

            InvoiceDetailsEntity invoiceDetails = invoiceDetailsService
                    .updateInvoiceDetails(user.getInvoiceDetails().getId(), invoiceDetailsDto);
            user.setInvoiceDetails(invoiceDetails);
        }

        DtoValidator.validate(dto);

        if (dto.getUsername() != null) {
            if (!user.getUsername().equals(dto.getUsername())) {
                checkIfUserExists(SignUpDto.builder().username(dto.getUsername()).build());
            }
            user.setUsername(dto.getUsername());
        }

        if (dto.getRole() != null) {
            user.setRole(dto.getRole());
        }

        if (dto.getEmail() != null) {
            if (!loggedUser.getEmail().equals(dto.getEmail())) {
                checkIfUserExists(SignUpDto.builder().email(dto.getEmail()).build());
            }
            user.setEmail(dto.getEmail());
        }

        if (dto.getNote() != null) {
            if (loggedUser.getRole() == UserRole.USER) {
                throw new NoPermissionException("You cannot add note for user");
            }
            user.setNote(dto.getNote());
        }

        userRepository.save(user);

        return userMapper.fromEntityToDto(user);
    }

    public void confirmEmail(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if (token == null) {
            throw new ResourceNotFoundException(
                    String.format(USER_DOESNT_EXIST_MESSAGE, confirmationToken));
        }

        UserEntity user = userRepository.findByEmailIgnoreCase(token.getEmail());

        user.setHasConfirmedEmail(true);
        userRepository.save(user);

    };

    public void sendPasswordResetLink(String email) {
        ConfirmationToken token = confirmationTokenRepository.findByEmail(email);

        if (token == null) {
            throw new ResourceNotFoundException(
                    String.format(USER_DOESNT_EXIST_MESSAGE, email));
        }

        String passwordResetUrl = frontendUrl + "/auth/reset-password?token="
                + token.getConfirmationToken();

        String mailMessage = "Click this link to reset your password: " + passwordResetUrl;

        mailService.sendMail("Reset your password", mailMessage, email);
    }

    public void resetPassword(String confirmationToken, ResetPasswordDto dto) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if (token == null) {
            throw new ResourceNotFoundException(
                    String.format(CONFIRMATION_TOKEN_DOESNT_EXIST_MESSAGE, confirmationToken));
        }

        UserEntity user = userRepository.findByEmailIgnoreCase(token.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));

        userRepository.save(user);
    }

    public void checkIfUserExists(SignUpDto signUpDto) throws ResourceAlreadyExistsException {
        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            throw new ResourceAlreadyExistsException(
                    String.format(USER_EXISTS_MESSAGE, signUpDto.getUsername()));
        }

        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            throw new ResourceAlreadyExistsException(
                    String.format(EMAIL_EXISTS_MESSAGE, signUpDto.getEmail()));
        }
    }

    public void checkIfUserDoesntExist(SignInDto signInDto) throws ResourceNotFoundException {
        if (!userRepository.existsByUsername(signInDto.getUsername())) {
            throw new ResourceNotFoundException(
                    String.format(USER_DOESNT_EXIST_MESSAGE, signInDto.getUsername()));
        }
    }

    public void checkIfUserHasConfirmedEmail(SignInDto signInDto) {
        boolean hasConfirmedEmail = userRepository.findByUsername(signInDto.getUsername()).get().hasConfirmedEmail();
        if (!hasConfirmedEmail) {
            throw new EmailNotConfirmedException(
                    String.format(USER_NOT_CONFIRMED_EMAIL_MESSAGE, signInDto.getUsername()));
        }
    }

}
