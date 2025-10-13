package com.example.demo.repository;

import com.example.demo.domain.Matriz.Matriz;
import com.example.demo.domain.disciplina.Disciplina;
import com.example.demo.domain.matriz_disciplina.MatrizDisciplina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatrizDisciplinaRepository extends JpaRepository<MatrizDisciplina, Long> {
    Optional<MatrizDisciplina> findByMatrizAndDisciplina(Matriz matriz, Disciplina disciplina);
    List<MatrizDisciplina> findByMatriz_Curso_Escola_Ativo(boolean ativo);
}
