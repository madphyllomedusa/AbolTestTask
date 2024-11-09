package ru.test.core.service.Impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.test.core.config.JwtService;
import ru.test.core.exceptionhandler.BadRequestException;
import ru.test.core.exceptionhandler.NotFoundException;
import ru.test.core.model.dto.JwtAuthenticationResponse;
import ru.test.core.model.dto.SignInRequest;
import ru.test.core.model.dto.SignUpRequest;
import ru.test.core.model.entity.User;
import ru.test.core.model.enums.Role;
import ru.test.core.model.mappers.UserMapper;
import ru.test.core.repository.UserRepository;
import ru.test.core.service.AuthService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    @Transactional
    public JwtAuthenticationResponse register(SignUpRequest signUpRequest) {
        logger.info("Registering user with email {}", signUpRequest.getEmail());

        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            logger.info("User already exists with email {}", signUpRequest.getEmail());
            throw new BadRequestException("Пользователь с этой почтой уже зарегистрирован");
        }

        if (!signUpRequest.getPassword().equals(signUpRequest.getMatchingPassword())) {
            throw new BadRequestException("Пароли не совпадают");
        }

        signUpRequest.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        if (signUpRequest.getRole() == null) {
            signUpRequest.setRole(Role.ROLE_USER);
        }


        User user = userMapper.toEntity(signUpRequest);
        User savedUser = userRepository.save(user);
        logger.info("User registered successfully: {}", savedUser.getUsername());

        String token = jwtService.generateToken(savedUser);
        return new JwtAuthenticationResponse(token);
    }

    @Override
    public JwtAuthenticationResponse login(SignInRequest signInRequest) {
        String identifier = signInRequest.getIdentifier().toLowerCase();
        String password = signInRequest.getPassword();
        logger.info("Attempting to login user with identifier {}", identifier);

        Optional<User> optionalUser = userRepository.findByEmail(identifier)
                .or(() -> userRepository.findByUsername(identifier));

        if (optionalUser.isEmpty()) {
            throw new NotFoundException("Пользователь не найден с идентификатором: " + identifier);
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException("Неверный пароль");
        }

        String token = jwtService.generateToken(user);
        logger.info("Generated token: {}", token);
        logger.info("User {} successfully logged in", optionalUser.get().getUsername());
        return new JwtAuthenticationResponse(token);

    }
}
