package com.example.demo.infra.Exception;

public class ProfessorHorarioJaExiste extends RegrasDeNegocioException {
    public ProfessorHorarioJaExiste(String message) {
        super(message);
    }
}