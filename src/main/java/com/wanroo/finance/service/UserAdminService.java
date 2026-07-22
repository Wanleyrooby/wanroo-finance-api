package com.wanroo.finance.service;

import com.wanroo.finance.dto.UserResponseDto;
import com.wanroo.finance.entity.User;
import com.wanroo.finance.exception.UserNotFoundException;
import com.wanroo.finance.mapper.UserMapper;
import com.wanroo.finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserAdminService {

    private final UserRepository userRepository;

    public Page<UserResponseDto> findAll(Pageable pageable) {

        return userRepository.findAll(pageable)
                .map(UserMapper::toResponse);
    }

    public UserResponseDto findById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        return UserMapper.toResponse(user);
    }
}
