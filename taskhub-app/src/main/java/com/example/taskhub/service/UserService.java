package com.example.taskhub.service;

import com.example.taskhub.dto.RegisterForm;
import com.example.taskhub.model.Role;
import com.example.taskhub.model.User;
import com.example.taskhub.repository.UserRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository users;
    private final PasswordEncoder encoder;

    public UserService(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return users.existsByEmail(email.trim().toLowerCase());
    }

    @Transactional(readOnly = true)
    public User getByEmail(String email) {
        return users.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new IllegalStateException("User not found: " + email));
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return users.findAll();
    }

    public User register(RegisterForm form) {
        return create(form.getFullName(), form.getEmail(), form.getPassword(), Role.USER);
    }

    public User create(String fullName, String email, String rawPassword, Role role) {
        User user = new User();
        user.setFullName(fullName.trim());
        user.setEmail(email.trim().toLowerCase());
        user.setPassword(encoder.encode(rawPassword));
        user.setRole(role);
        return users.save(user);
    }
}
