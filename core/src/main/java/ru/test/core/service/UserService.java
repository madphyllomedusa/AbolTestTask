package ru.test.core.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void blockUser(Long userId);
    void unblockUser(Long userId);
    UserDetails loadUserById(Long userId);
}
