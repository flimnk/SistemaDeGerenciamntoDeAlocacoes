package com.example.demo.infra.Exception;

public class CpfInvalidoException extends RegrasDeNegocioException {
    public CpfInvalidoException(String s) {
        super(s);
    }
}
