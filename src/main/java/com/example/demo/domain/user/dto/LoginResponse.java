package com.example.demo.domain.user.dto;

import com.example.demo.domain.professor.Professor;
import com.example.demo.domain.professor.dto.ProfessorResponse;

public record LoginResponse(
        String token,
        String role,
        ProfessorResponse professorResponse
) {

}