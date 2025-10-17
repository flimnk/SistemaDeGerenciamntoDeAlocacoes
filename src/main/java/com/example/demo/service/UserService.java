package com.example.demo.service;

import com.example.demo.domain.professor.Professor;
import com.example.demo.domain.user.Role;
import com.example.demo.domain.user.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User criarUsuarioParaProfessor(Professor professor) {

        User user = new User();
        user.setEmail(professor.getEmail().getEndereco());
        user.setPassword(passwordEncoder.encode(professor.getRegistro()));
        user.setRole(Role.PROFESSOR);

        user.setProfessor(professor);

        return userRepository.save(user);
    }
}
