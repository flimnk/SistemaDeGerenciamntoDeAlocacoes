package com.example.demo.domain.professor.dto;




import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;

import java.util.Set;

public record ProfessorCreateRequest(
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotNull(message = "O CPF é obrigatório")
        String cpf,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "O email informado não é válido")
        String email,

        @NotBlank(message = "O registro é obrigatório")
        String registro,

        @NotEmpty(message = "O id das escolas é obrigatório")
        Set<Long>escolasIds
) {}
