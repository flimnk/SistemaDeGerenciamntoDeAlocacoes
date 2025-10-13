package com.example.demo.repository;

import com.example.demo.domain.matriz_disciplina.MatrizDisciplina;
import com.example.demo.domain.prioridade.Prioridade;
import com.example.demo.domain.professor.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PrioridadeRepository extends JpaRepository<Prioridade, Long> {


    Optional<Prioridade> findByMatrizDisciplinaAndProfessor(MatrizDisciplina md, Professor professor);


    List<Prioridade> findByMatrizDisciplina_Matriz_Curso_Escola_Ativo(boolean ativo);
}