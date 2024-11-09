package ru.test.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.test.core.model.enums.Role;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private OffsetDateTime blockedAt;
    private List<String> imageUrls;

    @Override
    public String toString() {
        return "Username: " + username + " Email: " + email + " Role: " + role;
    }
}
