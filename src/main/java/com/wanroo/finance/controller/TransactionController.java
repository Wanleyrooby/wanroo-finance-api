package com.wanroo.finance.controller;

import com.wanroo.finance.dto.DashboardResponseDto;
import com.wanroo.finance.dto.TransactionRequestDto;
import com.wanroo.finance.dto.TransactionResponseDto;
import com.wanroo.finance.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Tag(name = "Transações", description = "Operações de gerenciamento de transações financeiras")
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(
            summary = "Cadastrar transação",
            description = "Cria uma nova transação financeira."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transação cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @PostMapping
    public ResponseEntity<TransactionResponseDto> create(
            @RequestBody @Valid TransactionRequestDto dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.create(dto));
    }

    @Operation(
            summary = "Listar transações",
            description = "Retorna uma lista paginada das transações do usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transações retornadas com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @GetMapping
    public ResponseEntity<Page<TransactionResponseDto>> findAll(
            @ParameterObject
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {

        return ResponseEntity.ok(transactionService.findAll(pageable));
    }

    @Operation(
            summary = "Buscar transação por ID",
            description = "Retorna uma transação específica."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transação encontrada"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "404", description = "Transação não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(transactionService.findById(id));
    }

    @Operation(
            summary = "Atualizar transação",
            description = "Atualiza uma transação existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transação atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "404", description = "Transação não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> update(
            @PathVariable Long id,
            @RequestBody @Valid TransactionRequestDto dto) {

        return ResponseEntity.ok(transactionService.update(id, dto));
    }

    @Operation(
            summary = "Excluir transação",
            description = "Remove uma transação do sistema."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Transação removida com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "404", description = "Transação não encontrada")
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}