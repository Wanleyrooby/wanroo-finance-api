package com.wanroo.finance.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDto(

        @NotBlank(message = "Nome é obligatório.")
        String name,

        String description
) {
}
