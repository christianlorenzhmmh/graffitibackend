package de.lorenzware.graffitibackend.service;

import de.lorenzware.graffitibackend.dto.LoginDto;
import de.lorenzware.graffitibackend.dto.RegistrationDto;
import de.lorenzware.graffitibackend.entity.UserEntity;
import de.lorenzware.graffitibackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserEntity register(RegistrationDto registrationDto) {
        if (userRepository.findByEmail(registrationDto.email).isPresent()) {
            throw new RuntimeException("email already exists");
        }
        UserEntity user = new UserEntity();
        user.setEmail(registrationDto.email);
        user.setPasswordHash(passwordEncoder.encode(registrationDto.password));
        user.setRole("STANDARD");
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public UserEntity authenticate(LoginDto loginDto) {
        UserEntity user = userRepository.findByEmail(loginDto.email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(loginDto.password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }
        return user;
    }
}
