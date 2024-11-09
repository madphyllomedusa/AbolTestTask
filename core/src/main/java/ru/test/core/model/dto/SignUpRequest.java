package ru.test.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.test.core.model.enums.Role;

@Data
@AllArgsConstructor
public class SignUpRequest {

    private String username;
    private String email;
    private String password;
    private String matchingPassword;
    private Role role;

    @Override
    public String toString() {
        return username;
    }

}
