package com.coremarker.demo_login.service;

import com.coremarker.demo_login.model.User;
import com.coremarker.demo_login.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static com.coremarker.demo_login.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest{

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;


    @Test
    void shouldReturnUser_successful() {
        User expectedUser = User.builder()
                .username(USERNAME)
                .email(EMAIL)
                .fullName(FULL_NAME)
                .build();

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.retrieveUser(USERNAME);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void retrieveUser_userNotFound() {
        // Arrange
        String username = "nonexistentuser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userService.retrieveUser(username));
    }
}
