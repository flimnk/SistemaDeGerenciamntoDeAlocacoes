package com.example.demo.controller;

import com.example.demo.domain.user.Role;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.dto.LoginRequest;
import com.example.demo.domain.user.dto.LoginResponse;
import com.example.demo.domain.user.dto.RegisterRequest;
import com.example.demo.infra.security.JwtService;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final PasswordEncoder passwordEncoder;
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) throws Exception {

        AuthenticationManager authenticationManager =
                authenticationConfiguration.getAuthenticationManager();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new LoginResponse(token, user.getRole().name()));
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {


        if (userRepository.findByEmail(request.email()).isPresent()) {
            return ResponseEntity.badRequest().body("Usuário já existe");
        }


        String encodedPassword = passwordEncoder.encode(request.password());

        User user = new User();

        user.setEmail(request.email());
        user.setPassword(encodedPassword);
        user.setRole(request.role().equalsIgnoreCase("ADMIN") ? Role.ADMIN : Role.PROFESSOR);

        userRepository.save(user);

        return ResponseEntity.ok("Usuário registrado com sucesso");
    }

}