package com.wanroo.finance.service;

import com.wanroo.finance.dto.UpdateUserDto;
import com.wanroo.finance.dto.UserResponseDto;
import com.wanroo.finance.entity.User;
import com.wanroo.finance.mapper.UserMapper;
import com.wanroo.finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public UserResponseDto myProfile() {
        User user = getAuthenticatedUser();

        return UserMapper.toResponse(user);
    }

    public UserResponseDto updateProfile(UpdateUserDto dto) {
        User user = getAuthenticatedUser();

        user.setName(dto.name());
        user.setEmail(dto.email());

        User updatedUser = repository.save(user);

        return UserMapper.toResponse(updatedUser);
    }

    private User getAuthenticatedUser() {
        throw new UnsupportedOperationException(
                "Método ainda não implementado"
        );
    }
}
