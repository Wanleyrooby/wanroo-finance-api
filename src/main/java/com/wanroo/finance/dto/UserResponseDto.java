package com.wanroo.finance.dto;

import com.wanroo.finance.entity.Role;

import java.time.Instant;

public record UserResponseDto(
        Long id,
        String name,
        String email,
        Role role,
        Instant createdAt
) {
}
