package ru.test.core.service;

import ru.test.core.model.dto.JwtAuthenticationResponse;
import ru.test.core.model.dto.SignInRequest;
import ru.test.core.model.dto.SignUpRequest;

public interface AuthService {
    JwtAuthenticationResponse register(SignUpRequest signUpRequest);
    JwtAuthenticationResponse login(SignInRequest signInRequest);
}
