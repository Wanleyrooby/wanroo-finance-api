package com.wanroo.finance.service;

import com.wanroo.finance.dto.UpdateUserDto;
import com.wanroo.finance.dto.UserResponseDto;
import com.wanroo.finance.entity.User;
import com.wanroo.finance.mapper.UserMapper;
import com.wanroo.finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDto myProfile() {
        User user = getAuthenticatedUser();

        return UserMapper.toResponse(user);
    }

    public UserResponseDto updateProfile(UpdateUserDto dto) {

        User user = getAuthenticatedUser();

        if (!user.getEmail().equals(dto.email())
                && userRepository.existsByEmail(dto.email())) {

            throw new RuntimeException("Email já cadastrado.");
        }

        user.setName(dto.name());
        user.setEmail(dto.email());

        User updatedUser = userRepository.save(user);

        return UserMapper.toResponse(updatedUser);
    }

    public List<UserResponseDto> findAll() {

        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    private User getAuthenticatedUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public UserResponseDto findById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return UserMapper.toResponse(user);
    }
}
