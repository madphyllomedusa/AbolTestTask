package ru.test.core.service.Impl;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.test.core.config.JwtService;
import ru.test.core.exceptionhandler.BadRequestException;
import ru.test.core.exceptionhandler.NotFoundException;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    @Transactional
    public UserDto register(UserDto userDto) {
        logger.info("Saving user {}", userDto);
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            logger.info("User already exists with email {}", userDto.getEmail());
            throw new BadRequestException("Email уже используется");
        }

        if (!userDto.getPassword().equals(userDto.getMatchingPassword())) {
            throw new BadRequestException("Пароли не совпадают");
        }

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        if (userDto.getRole() == null) {
            userDto.setRole(Role.USER);
        }

        if (userDto.getUsername() == null
                || userDto.getUsername().isEmpty()
                || userDto.getEmail() == null
                || userDto.getEmail().isEmpty()) {
            logger.warn("Some user fields is empty or null {}", userDto);
            throw new BadRequestException("Some user fields is empty or null");

        }
        User user = userMapper.toEntity(userDto);
        User savedUser = userRepository.save(user);
        logger.info("Saved user {}", savedUser);
        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional
    public JwtAuthenticationResponse login(SignInRequest signInRequest) {
        String identifier = signInRequest.getIdentifier().toLowerCase();
        String password = signInRequest.getPassword();
        logger.info("Attempting to login user with identifier {}", identifier);

        Optional<User> optionalUser = userRepository.findByEmail(identifier);

        if (optionalUser.isEmpty()) {
            optionalUser = userRepository.findByUsername(identifier);
        }

        if (optionalUser.isEmpty()) {
            throw new NotFoundException("Пользователь не найден с идентификатором: " + identifier);
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException("Неверный пароль");
        }

        UserDetails userDetails = userMapper.toUserDetails(user);
        String token = jwtService.generateToken(userDetails);
        logger.info("Generated token: {}", token);
        logger.info("User {} successfully logged in", optionalUser.get().getUsername());
        return new JwtAuthenticationResponse(token);

    }
}
