package com.coremarker.demo_login.service;

import com.coremarker.demo_login.DTO.LoginUserDTO;
import com.coremarker.demo_login.DTO.RegisterUserDTO;
import com.coremarker.demo_login.exception.NoUserFoundException;
import com.coremarker.demo_login.model.User;
import com.coremarker.demo_login.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.coremarker.demo_login.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthUserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthUserService authUserService;

    @Test
    void userShouldRegister_successful() {
        //Mocking part
        RegisterUserDTO registerUser = new RegisterUserDTO(USERNAME, EMAIL, PASSWORD, FULL_NAME);
        String encodedPassword = "encodedPassword";
        User expectedUser = getMockUserFromInput(registerUser, encodedPassword);

        when(passwordEncoder.encode(registerUser.password())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(expectedUser
                .toBuilder()
                .userId(1L)
                .build());

        User actualUser = authUserService.register(registerUser);

        assertNotNull(actualUser);
        assertEquals(1L, actualUser.getUserId());
        assertEquals(registerUser.username(), actualUser.getUsername());
        assertEquals(registerUser.email(), actualUser.getEmail());
        assertEquals(registerUser.fullName(), actualUser.getFullName());
        assertEquals(encodedPassword, actualUser.getPassword());

        verify(passwordEncoder).encode(registerUser.password());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void userShouldAuthenticate_successful() throws NoUserFoundException {
        LoginUserDTO loginUser = new LoginUserDTO(USERNAME, PASSWORD);
        User expectedUser = User.builder()
                .username(USERNAME)
                .password("encodedPassword")
                .build();

        when(userRepository.findByUsername(loginUser.username())).thenReturn(Optional.of(expectedUser));

        User actualUser = authUserService.authenticate(loginUser);

        assertEquals(expectedUser, actualUser);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class)); // Verify authentication
    }

    @Test
    void userShouldNotAuthenticate_userNotFound() {
        LoginUserDTO loginUser = new LoginUserDTO("nonexistentuser", "password");

        when(userRepository.findByUsername(loginUser.username())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoUserFoundException.class, () -> authUserService.authenticate(loginUser));
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    private static User getMockUserFromInput(RegisterUserDTO registerUserDTO, String encodedPassword) {
        return User.builder()
                .username(registerUserDTO.username())
                .password(encodedPassword)
                .email(registerUserDTO.email())
                .fullName(registerUserDTO.fullName())
                .build();
    }
}
