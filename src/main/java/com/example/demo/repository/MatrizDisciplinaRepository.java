package com.example.demo.repository;

import com.example.demo.domain.matriz_disciplina.MatrizDisciplina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatrizDisciplinaRepository extends JpaRepository<MatrizDisciplina, Long> {
}