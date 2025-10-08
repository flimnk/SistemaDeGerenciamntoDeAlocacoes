package com.example.demo.infra.Tratador;

import com.example.demo.infra.Exception.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private record ErrorDetails(
            Instant timestamp,
            HttpStatus status,
            String error,
            String message,
            String path
    ) {}

    // ------------------------------------------------------------------------------------------
    // 1. TRATAMENTO DE ERROS DE VALIDAÇÃO DE ENTRADA (BAD REQUEST - 400)
    // Lida com @Valid ou @Validated no DTO, onde o Spring detecta erros.
    // ------------------------------------------------------------------------------------------

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        // Coleta e formata as mensagens de erro de validação
        String detailedMessage = ex.getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));

        HttpStatus status = HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(new ErrorDetails(
                Instant.now(),
                status,
                "Validation Error",
                "Erros de validação nos campos: " + detailedMessage,
                request.getRequestURI()
        ));
    }


    // ------------------------------------------------------------------------------------------
    // 2. TRATAMENTO DE EXCEÇÕES DE DOMÍNIO (NEGÓCIO) (BAD REQUEST/CONFLICT - 400/409)
    // Lida com suas exceções específicas que indicam falha na regra de negócio.
    // ------------------------------------------------------------------------------------------

    // Tratamento para regras que falham antes de persistir (Ex: CPF, Email Inválido) -> 400
    @ExceptionHandler({
            CpfInvalidoException.class,
            EmailInvalidoException.class,
            HorarioInicioMaiorHorarioFinalExcpetion.class,
            RegrasDeNegocioException.class ,
            HorarioForaDoTurnoException.class
    })
    public ResponseEntity<ErrorDetails> handleBadRequestExceptions(
            RuntimeException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(new ErrorDetails(
                Instant.now(),
                status,
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                request.getRequestURI()
        ));
    }

    // Tratamento para conflitos de recursos (Ex: Já Existe) -> 409
    @ExceptionHandler({
            HorarioJaExisteException.class,
            DisciplinaJaExisteException.class
    })
    public ResponseEntity<ErrorDetails> handleConflictExceptions(
            RuntimeException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.CONFLICT; // 409 Conflict
        return ResponseEntity.status(status).body(new ErrorDetails(
                Instant.now(),
                status,
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                request.getRequestURI()
        ));
    }

    // ------------------------------------------------------------------------------------------
    // 3. TRATAMENTO DE RECURSOS NÃO ENCONTRADOS (NOT FOUND - 404)
    // Lida com tentativas de buscar/atualizar recursos inexistentes.
    // ------------------------------------------------------------------------------------------

    @ExceptionHandler({
            EntityNotFoundException.class, // Padrão do JPA
            DisciplinaNotFoundException.class // Sua exceção específica
    })
    public ResponseEntity<ErrorDetails> handleNotFoundExceptions(
            RuntimeException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.NOT_FOUND; // 404 Not Found
        return ResponseEntity.status(status).body(new ErrorDetails(
                Instant.now(),
                status,
                "Resource Not Found",
                ex.getMessage(),
                request.getRequestURI()
        ));
    }

    // ------------------------------------------------------------------------------------------
    // 4. TRATAMENTO GENÉRICO (INTERNAL SERVER ERROR - 500)
    // Última linha de defesa para erros inesperados.
    // ------------------------------------------------------------------------------------------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGenericErrors(Exception ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500

        // Em produção, você deve logar 'ex' mas retornar uma mensagem genérica para o cliente
        return ResponseEntity.status(status).body(new ErrorDetails(
                Instant.now(),
                status,
                "Internal Server Error",
                "Ocorreu um erro inesperado no servidor. Tente novamente mais tarde.",
                request.getRequestURI()
        ));
    }

    // Adicione este método ao seu ControllerAdvice
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDetails> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        Throwable rootCause = ex.getRootCause(); // Tenta encontrar a causa raiz

        // 1. Verifica se a causa raiz é a sua exceção de regra de negócio
        if (rootCause instanceof HorarioInicioMaiorHorarioFinalExcpetion) {
            HorarioInicioMaiorHorarioFinalExcpetion domainEx = (HorarioInicioMaiorHorarioFinalExcpetion) rootCause;

            // Retorna 400 com os detalhes da sua exceção
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDetails(
                    Instant.now(),
                    HttpStatus.BAD_REQUEST,
                    domainEx.getClass().getSimpleName(),
                    domainEx.getMessage(),
                    request.getRequestURI()
            ));
        }

        // 2. Para todos os outros erros de parsing (JSON malformado, tipo errado, etc.)
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(new ErrorDetails(
                Instant.now(),
                status,
                ex.getClass().getSimpleName(),
                "Corpo da requisição inválido. Verifique o formato do JSON.", // Mensagem genérica mais amigável
                request.getRequestURI()
        ));
    }
}