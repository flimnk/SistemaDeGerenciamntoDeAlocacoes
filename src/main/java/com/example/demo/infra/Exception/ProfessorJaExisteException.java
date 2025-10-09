package com.example.demo.infra.Exception;

public class ProfessorJaExisteException extends RegrasDeNegocioException {
    public ProfessorJaExisteException(String detalhe) {
        super( detalhe);
    }
}
