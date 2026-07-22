package com.wanroo.finance.controller;

import com.wanroo.finance.dto.UpdateUserDto;
import com.wanroo.finance.dto.UserResponseDto;
import com.wanroo.finance.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Operações relacionadas ao perfil do usuário autenticado")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Consultar perfil",
            description = "Retorna os dados do usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil retornado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> myProfile() {
        return ResponseEntity.ok(userService.myProfile());
    }

    @Operation(
            summary = "Atualizar perfil",
            description = "Atualiza os dados do usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @PutMapping("/me")
    public ResponseEntity<UserResponseDto> updateProfile(
            @RequestBody @Valid UpdateUserDto dto) {

        return ResponseEntity.ok(userService.updateProfile(dto));
    }
}