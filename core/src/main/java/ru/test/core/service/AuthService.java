package ru.test.core.service;

import ru.test.core.model.dto.JwtAuthenticationResponse;
import ru.test.core.model.dto.SignInRequest;
import ru.test.core.model.dto.UserDto;

public interface AuthService {
    UserDto register(UserDto userDto);
    JwtAuthenticationResponse login(SignInRequest signInRequest);
}
