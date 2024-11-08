package ru.test.core.model.mappers;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
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
        userDto.setPassword(user.getPassword());
        userDto.setRole(user.getRole());
        userDto.setBlock(user.getBlocked());
        return userDto;
    }

    public User toEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername().toLowerCase());
        user.setEmail(userDto.getEmail().toLowerCase());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());
        user.setBlocked(userDto.getBlock());
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
