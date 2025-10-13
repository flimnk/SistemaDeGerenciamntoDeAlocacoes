package com.example.demo.repository;

import com.example.demo.domain.curso.Curso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    boolean existsByNome(String nome);

    Page<Curso> findByEscola_Ativo(boolean ativo, Pageable pageable);
}