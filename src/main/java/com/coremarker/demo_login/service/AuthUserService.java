package com.coremarker.demo_login.service;

import com.coremarker.demo_login.DTO.LoginUserDTO;
import com.coremarker.demo_login.DTO.RegisterUserDTO;
import com.coremarker.demo_login.exception.NoUserFoundException;
import com.coremarker.demo_login.model.User;
import com.coremarker.demo_login.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthUserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public User register(RegisterUserDTO registerUser) {
        User user = User.builder()
                .username(registerUser.username())
                .password(passwordEncoder.encode(registerUser.password()))
                .email(registerUser.email())
                .fullName(registerUser.fullName())
                .build();

        return userRepository.save(user);
    }

    public User authenticate(LoginUserDTO loginUser) throws NoUserFoundException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUser.username(), loginUser.password()));

        return userRepository.findByUsername(loginUser.username())
                .orElseThrow(() -> new NoUserFoundException("User " + loginUser.username() + " not found in database."));
    }
}
