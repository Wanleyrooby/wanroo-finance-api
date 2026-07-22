package com.wanroo.finance.dto;

import com.wanroo.finance.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Dados de resposta de um usuário")
public record UserResponseDto(

        @Schema(
                description = "Identificador único do usuário",
                example = "1"
        )
        Long id,

        @Schema(
                description = "Nome completo do usuário",
                example = "Wanley Alexis"
        )
        String name,

        @Schema(
                description = "Email utilizado para autenticação",
                example = "wanley@email.com"
        )
        String email,

        @Schema(
                description = "Perfil de acesso do usuário",
                example = "USER"
        )
        Role role,

        @Schema(
                description = "Data e hora de criação do usuário",
                example = "2026-07-21T18:30:00Z"
        )
        Instant createdAt

) {
}