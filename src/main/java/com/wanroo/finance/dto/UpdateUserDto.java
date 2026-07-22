package com.wanroo.finance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para atualização do perfil do usuário")
public record UpdateUserDto(

        @Schema(
                description = "Nome completo do usuário",
                example = "João da Silva"
        )
        @Size(
                min = 3,
                max = 100,
                message = "Nome deve ter entre 3 e 100 caracteres"
        )
        String name,

        @Schema(
                description = "Endereço de e-mail do usuário",
                example = "joao.silva@email.com"
        )
        @Email(message = "Email deve ser válido")
        String email
) {
}