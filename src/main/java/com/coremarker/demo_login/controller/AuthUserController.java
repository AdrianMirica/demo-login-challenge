package com.coremarker.demo_login.controller;

import com.coremarker.demo_login.DTO.LoginResponseDTO;
import com.coremarker.demo_login.DTO.LoginUserDTO;
import com.coremarker.demo_login.DTO.RegisterUserDTO;
import com.coremarker.demo_login.DTO.UserDTO;
import com.coremarker.demo_login.exception.NoUserFoundException;
import com.coremarker.demo_login.model.User;
import com.coremarker.demo_login.service.AuthUserService;
import com.coremarker.demo_login.service.JWTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.ZoneId;

@Slf4j
@RestController()
@RequestMapping(value = "/auth")
public class AuthUserController {
    private final JWTService jwtService;
    private final AuthUserService authUserService;

    public AuthUserController(final JWTService jwtService, final AuthUserService authUserService) {
        this.jwtService = jwtService;
        this.authUserService = authUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody final RegisterUserDTO registerUserDetails) {
        log.debug("Input details to register user: {}", registerUserDetails);
        final User registeredUser = authUserService.register(registerUserDetails);
        final String registerResponse = registeredUser != null ?
                "User " + registeredUser.getUsername() + " was created with success"
                : "There was an issue registering the user with details: " + registerUserDetails;
        log.info("Registration response: {}", mapUserToUserDTO(registeredUser));
        return ResponseEntity.ok(registerResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody final LoginUserDTO loginUser) throws NoUserFoundException {
        log.debug("Login details for user: {}", loginUser);
        final User authenticatedUser = authUserService.authenticate(loginUser);
        final String jwtToken = jwtService.generateToken(authenticatedUser);
        final long expirationTime = jwtService.getJwtExpirationTime();
        final LoginResponseDTO loginResponse = new LoginResponseDTO(jwtToken, Instant.now().plusMillis(expirationTime)
                .atZone(ZoneId.of("Europe/Bucharest")).toLocalDateTime());
        log.info("Login response: {}", loginResponse);
        return ResponseEntity.ok(loginResponse);
    }

    private static UserDTO mapUserToUserDTO(User user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(
                user.getUsername(),
                user.getEmail(),
                user.getFullName()
        );
    }
}
