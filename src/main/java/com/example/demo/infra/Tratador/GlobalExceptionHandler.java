// Local: com.example.demo.exceptions/GlobalExceptionHandler.java

package com.example.demo.infra.Tratador;

import com.example.demo.infra.Exception.DisciplinaJaExisteException;
import com.example.demo.infra.Exception.DisciplinaNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(DisciplinaNotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(DisciplinaNotFoundException ex) {

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND); 
    }


    @ExceptionHandler(DisciplinaJaExisteException.class)
    public ResponseEntity<String> handleConflictException(DisciplinaJaExisteException ex) {

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT); 
    }
    

}