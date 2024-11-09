package ru.test.core.model.mappers;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.test.core.model.dto.SignUpRequest;
import ru.test.core.model.dto.UserDto;
import ru.test.core.model.entity.User;

import java.util.Collections;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername().toLowerCase());
        userDto.setEmail(user.getEmail().toLowerCase());
        userDto.setRole(user.getRole());
        userDto.setBlockedAt(user.getBlockedAt());
        return userDto;
    }

    public User toEntity(SignUpRequest signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.getUsername().toLowerCase());
        user.setEmail(signUpRequest.getEmail().toLowerCase());
        user.setPassword(signUpRequest.getPassword());
        user.setRole(signUpRequest.getRole());
        return user;
    }

    public UserDetails toUserDetails(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getEmail().toLowerCase(),
                user.getPassword(),
                Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + user.getRole()
                                                                        .name()))
        );
    }
}
