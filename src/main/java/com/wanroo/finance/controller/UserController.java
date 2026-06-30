package com.wanroo.finance.controller;

import com.wanroo.finance.dto.UpdateUserDto;
import com.wanroo.finance.dto.UserResponseDto;
import com.wanroo.finance.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> myProfile() {
        return ResponseEntity.ok(userService.myProfile());
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDto> updateProfile(@RequestBody @Valid UpdateUserDto dto) {
        return ResponseEntity.ok(userService.updateProfile(dto));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> findAll() {

        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(userService.findById(id));
    }
}
