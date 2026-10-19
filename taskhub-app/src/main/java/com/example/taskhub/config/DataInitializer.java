package com.example.taskhub.config;

import com.example.taskhub.model.Role;
import com.example.taskhub.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** Creates a default admin account the first time the app starts. */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService users;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    public DataInitializer(UserService users) {
        this.users = users;
    }

    @Override
    public void run(String... args) {
        if (!users.emailExists(adminEmail)) {
            users.create("Administrator", adminEmail, adminPassword, Role.ADMIN);
            System.out.println(">>> Default admin created: " + adminEmail);
        }
    }
}
