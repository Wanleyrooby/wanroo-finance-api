package com.wanroo.finance.mapper;

import com.wanroo.finance.dto.UserRequestDto;
import com.wanroo.finance.dto.UserResponseDto;
import com.wanroo.finance.entity.Role;
import com.wanroo.finance.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static User toEntity(UserRequestDto dto) {
        return User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(dto.password())
                .role(Role.USER)
                .build();
    }

    public static UserResponseDto toResponse(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
