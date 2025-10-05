// Local: com.example.demo.exceptions/DisciplinaNotFoundException.java

package com.example.demo.infra.Exception;

public class DisciplinaNotFoundException extends RuntimeException {

    public DisciplinaNotFoundException(Long id) {
        super("Disciplina com ID " + id + " não foi encontrada.");
    }
}