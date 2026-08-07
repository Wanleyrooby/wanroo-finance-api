package com.wanroo.finance.service;

import com.wanroo.finance.dto.UpdateUserDto;
import com.wanroo.finance.dto.UserResponseDto;
import com.wanroo.finance.entity.Role;
import com.wanroo.finance.entity.User;
import com.wanroo.finance.exception.EmailAlreadyExistsException;
import com.wanroo.finance.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticatedUserService authenticatedUserService;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setup() {

        user = User.builder()
                .id(1L)
                .name("Wanley Alexis")
                .email("wanley@email.com")
                .password("12345678")
                .role(Role.USER)
                .createdAt(Instant.now())
                .build();
    }


    @Test
    void shouldReturnAuthenticatedUserProfile() {

        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(user);

        UserResponseDto response =
                userService.myProfile();

        assertNotNull(response);

        assertEquals(user.getId(), response.id());
        assertEquals(user.getName(), response.name());
        assertEquals(user.getEmail(), response.email());
        assertEquals(user.getRole(), response.role());

        verify(authenticatedUserService)
                .getAuthenticatedUser();
    }



    @Test
    void shouldUpdateUserProfileSuccessfully() {

        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(user);

        UpdateUserDto dto =
                new UpdateUserDto(
                        "Wanley Rooby",
                        "wanley.rooby@email.com"
                );

        when(userRepository.save(user))
                .thenReturn(user);

        UserResponseDto response =
                userService.updateProfile(dto);

        assertNotNull(response);

        assertEquals(
                "Wanley Rooby",
                response.name()
        );

        assertEquals(
                "wanley.rooby@email.com",
                response.email()
        );

        verify(userRepository)
                .save(user);
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(user);

        UpdateUserDto dto =
                new UpdateUserDto(
                        "Wanley Alexis",
                        "outro@email.com"
                );

        when(userRepository.existsByEmail(dto.email()))
                .thenReturn(true);

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> userService.updateProfile(dto)
        );

        verify(userRepository, never())
                .save(any());
    }
}