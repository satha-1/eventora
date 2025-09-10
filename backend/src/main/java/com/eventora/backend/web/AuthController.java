package com.eventora.backend.web;

import com.eventora.backend.domain.Role;
import com.eventora.backend.domain.User;
import com.eventora.backend.repository.UserRepository;
import com.eventora.backend.security.JwtService;
import com.eventora.backend.web.dto.AuthResponse;
import com.eventora.backend.web.dto.LoginRequest;
import com.eventora.backend.web.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
        if (userRepository.findByEmail(req.email()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        User u = new User();
        u.setName(req.name());
        u.setEmail(req.email());
        u.setPassword(passwordEncoder.encode(req.password()));
        try {
            u.setRole(Role.valueOf(req.role() == null ? "ATTENDEE" : req.role().toUpperCase()));
        } catch (Exception e) {
            u.setRole(Role.ATTENDEE);
        }
        userRepository.save(u);
        String token = jwtService.generateToken(u.getEmail(), Map.of("role", u.getRole().name(), "name", u.getName()));
        return ResponseEntity.ok(new AuthResponse(token, u.getName(), u.getRole().name()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.email(), req.password()));
        User u = userRepository.findByEmail(req.email()).orElseThrow();
        String token = jwtService.generateToken(u.getEmail(), Map.of("role", u.getRole().name(), "name", u.getName()));
        return ResponseEntity.ok(new AuthResponse(token, u.getName(), u.getRole().name()));
    }
}
