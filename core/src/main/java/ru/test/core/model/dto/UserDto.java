package ru.test.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.test.core.model.enums.Role;

import java.util.List;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String matchingPassword;
    private Role role;
    private List<String> imageUrls;

    @Override
    public String toString() {
        return "Username: " + username + " Email: " + email + " Role: " + role;
    }
}
