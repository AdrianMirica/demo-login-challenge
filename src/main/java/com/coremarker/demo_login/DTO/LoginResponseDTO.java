package com.coremarker.demo_login.DTO;

import java.time.LocalDateTime;

public record LoginResponseDTO(String token, LocalDateTime expirationTime) {
}
