package de.lorenzware.graffitibackend.controller.api;

import de.lorenzware.graffitibackend.dto.JwtResponse;
import de.lorenzware.graffitibackend.dto.LoginDto;
import de.lorenzware.graffitibackend.dto.RegistrationDto;
import de.lorenzware.graffitibackend.entity.UserEntity;
import de.lorenzware.graffitibackend.service.JwtUtil;
import de.lorenzware.graffitibackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationDto req) {
        UserEntity user = userService.register(req);
        String token = jwtUtil.generateToken(user);
        return ResponseEntity.ok(new JwtResponse(token, user.getRole(), user.getId()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        UserEntity user = userService.authenticate(loginDto);
        String token = jwtUtil.generateToken(user);
        return ResponseEntity.ok(new JwtResponse(token, user.getRole(), user.getId()));
    }
}
