package com.wanroo.finance.service;

import com.wanroo.finance.dto.AuthResponseDto;
import com.wanroo.finance.dto.LoginRequestDto;
import com.wanroo.finance.dto.UserRequestDto;
import com.wanroo.finance.dto.UserResponseDto;
import com.wanroo.finance.entity.Role;
import com.wanroo.finance.entity.User;
import com.wanroo.finance.mapper.UserMapper;
import com.wanroo.finance.repository.UserRepository;
import com.wanroo.finance.security.CustomUserDetailsService;
import com.wanroo.finance.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtService jwtService;

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

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.email(),
                        dto.password()
                )
        );

        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername(dto.email());

        String token = jwtService.generateToken(userDetails);

        return new AuthResponseDto(token);
    }
}