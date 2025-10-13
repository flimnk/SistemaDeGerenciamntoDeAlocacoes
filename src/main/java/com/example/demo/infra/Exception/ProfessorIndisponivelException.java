package com.example.demo.infra.Exception;

public class ProfessorIndisponivelException extends RegrasDeNegocioException{
    public ProfessorIndisponivelException(String message) {
        super(message);
    }
}