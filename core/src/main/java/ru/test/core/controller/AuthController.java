package ru.test.core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.test.core.model.dto.JwtAuthenticationResponse;
import ru.test.core.model.dto.SignInRequest;
import ru.test.core.model.dto.SignUpRequest;
import ru.test.core.model.dto.UserDto;
import ru.test.core.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<JwtAuthenticationResponse> register(@RequestBody SignUpRequest signUpRequest) {
        JwtAuthenticationResponse jwtResponse = authService.register(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(jwtResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody SignInRequest signInRequest) {
        JwtAuthenticationResponse jwtResponse = authService.login(signInRequest);
        return ResponseEntity.status(HttpStatus.OK).body(jwtResponse);
    }

}
