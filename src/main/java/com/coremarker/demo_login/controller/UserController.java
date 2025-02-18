package com.coremarker.demo_login.controller;

import com.coremarker.demo_login.DTO.UserDTO;
import com.coremarker.demo_login.model.User;
import com.coremarker.demo_login.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        log.debug("Querying database for user with username: {}", username);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            if (username.equals(user.getUsername())) {
                return ResponseEntity.ok(mapUserToUserDTO(user));
            } else {
                User dbUser = userService.retrieveUser(username);
                return ResponseEntity.ok(mapUserToUserDTO(dbUser));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User meAsUser = (User) authentication.getPrincipal();
        log.debug("Authenticated user: {}", mapUserToUserDTO(meAsUser));

        return ResponseEntity.ok(mapUserToUserDTO(meAsUser));
    }

    private static UserDTO mapUserToUserDTO(User user) {
        return new UserDTO(
                user.getUsername(),
                user.getEmail(),
                user.getFullName()
        );
    }
}
