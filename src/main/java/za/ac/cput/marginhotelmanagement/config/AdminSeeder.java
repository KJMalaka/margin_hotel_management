package za.ac.cput.marginhotelmanagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import za.ac.cput.marginhotelmanagement.domain.AppUser;
import za.ac.cput.marginhotelmanagement.enums.UserRole;
import za.ac.cput.marginhotelmanagement.repository.AppUserRepository;

/*
   Creates one ADMIN account on startup, only if it doesn't already exist.
   */

@Configuration
public class AdminSeeder {

    @Bean
    CommandLineRunner seedAdminUser(AppUserRepository userRepository, PasswordEncoder passwordEncoder,
                                    @Value("${app.admin.email}") String email,
                                    @Value("${app.admin.password}") String password) {
        return args -> {
            if (userRepository.findByEmail(email).isEmpty()) {
                // Create and save the admin user
                AppUser adminUser = new AppUser.Builder()
                        .setEmail(email)
                        .setPassword(passwordEncoder.encode(password))
                        .setRole(UserRole.ADMIN)
                        .build();
                userRepository.save(adminUser);
                System.out.println("Created admin default account for: " + email);
            }
        };
    }
}