package com.wanroo.finance.service;

import com.wanroo.finance.dto.AuthResponseDto;
import com.wanroo.finance.dto.LoginRequestDto;
import com.wanroo.finance.dto.UserRequestDto;
import com.wanroo.finance.dto.UserResponseDto;
import com.wanroo.finance.entity.Role;
import com.wanroo.finance.entity.User;
import com.wanroo.finance.mapper.UserMapper;
import com.wanroo.finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto register(UserRequestDto dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new RuntimeException("Email já cadastrado.");
        }

        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);

        return UserMapper.toResponse(savedUser);
    }

    public AuthResponseDto login(LoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new RuntimeException("Credenciais inválidas"));

        if (!passwordEncoder.matches(
                dto.password(),
                user.getPassword())) {

            throw new RuntimeException("Credenciais inválidas");
        }

        // Quando implementar JWT
        String token = "JWT_TOKEN_AQUI";

        return new AuthResponseDto(token);


    }
}
