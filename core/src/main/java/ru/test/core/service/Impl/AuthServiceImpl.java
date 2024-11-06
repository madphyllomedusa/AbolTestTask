package ru.test.core.service.Impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import ru.test.core.model.dto.JwtAuthenticationResponse;
import ru.test.core.model.dto.SignInRequest;
import ru.test.core.model.dto.UserDto;
import ru.test.core.model.entity.User;
import ru.test.core.model.enums.Role;
import ru.test.core.model.mappers.UserMapper;
import ru.test.core.repository.UserRepository;
import ru.test.core.service.AuthService;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto register(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()).isPresent() ||
                userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Username or Email already exists");
        }

        User user = userMapper.toEntity(userDto);
        //user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    public JwtAuthenticationResponse login(SignInRequest signInRequest) {
        Optional<User> userOpt = userRepository.findByUsername(signInRequest.getIdentifier())
                .or(() -> userRepository.findByEmail(signInRequest.getIdentifier()));

        return null;

    }
}
