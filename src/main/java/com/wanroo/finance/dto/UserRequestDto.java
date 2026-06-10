package com.wanroo.finance.dto;

public record UserRequestDto(
        String name,
        String email,
        String password
) {
}