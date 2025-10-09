package com.example.demo.repository;

import com.example.demo.domain.professor.Professor;
import com.example.demo.domain.vo.Cpf;
import com.example.demo.domain.vo.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    boolean existsByCpf(Cpf cpf);
    boolean existsByEmail(Email email);
    boolean existsByRegistro(String registro);

    boolean existsByEmailAndIdNot(Email email, Long id);
}