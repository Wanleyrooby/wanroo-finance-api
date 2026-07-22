package com.wanroo.finance.service;

import com.wanroo.finance.dto.UpdateUserDto;
import com.wanroo.finance.dto.UserResponseDto;
import com.wanroo.finance.entity.User;
import com.wanroo.finance.exception.EmailAlreadyExistsException;
import com.wanroo.finance.exception.UserNotFoundException;
import com.wanroo.finance.mapper.UserMapper;
import com.wanroo.finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticatedUserService authenticatedUserService;

    public UserResponseDto myProfile() {
        User user = authenticatedUserService.getAuthenticatedUser();

        return UserMapper.toResponse(user);
    }

    public UserResponseDto updateProfile(UpdateUserDto dto) {

        User user = authenticatedUserService.getAuthenticatedUser();

        if (!user.getEmail().equals(dto.email())
                && userRepository.existsByEmail(dto.email())) {

            throw new EmailAlreadyExistsException();
        }

        user.setName(dto.name());
        user.setEmail(dto.email());

        User updatedUser = userRepository.save(user);

        return UserMapper.toResponse(updatedUser);
    }
}
