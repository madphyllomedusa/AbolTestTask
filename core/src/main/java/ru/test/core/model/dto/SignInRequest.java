package ru.test.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInRequest {
    private String identifier;
    private String password;

    @Override
    public String toString() {
        return identifier;
    }
}
