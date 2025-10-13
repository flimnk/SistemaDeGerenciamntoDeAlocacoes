package com.example.demo.infra.Exception;

public class AlocacaoJaExisteException extends RegrasDeNegocioException {
    public AlocacaoJaExisteException(String message) {
        super(message);
    }
}