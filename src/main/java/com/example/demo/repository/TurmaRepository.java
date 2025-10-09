package com.example.demo.repository;

import com.example.demo.domain.turma.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {
    boolean existsByCodigo(String codigo);

    Optional<Turma> findByCodigo(String codigo);
}