package com.wanroo.finance.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta da autenticação contendo o token JWT")
public record AuthResponseDto(

        @Schema(
                description = "Token JWT utilizado para autenticação nos endpoints protegidos",
                example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwanVAZW1haWwuY29tIiwiaWF0IjoxNzU1MDAwMDAwLCJleHAiOjE3NTUwMDM2MDB9.xxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
        )
        String token
) {
}