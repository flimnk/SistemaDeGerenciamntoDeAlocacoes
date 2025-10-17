package com.example.demo.repository;

import com.example.demo.domain.matrizDisciplina.MatrizDisciplina;
import com.example.demo.domain.prioridade.Prioridade;
import com.example.demo.domain.prioridade.PrioridadeNivel;
import com.example.demo.domain.prioridade.dto.DisciplinaContagemDTO;

import com.example.demo.domain.professor.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PrioridadeRepository extends JpaRepository<Prioridade, Long> {


    Optional<Prioridade> findByMatrizDisciplinaAndProfessor(MatrizDisciplina md, Professor professor);

    // PrioridadeRepository.java
    List<Prioridade> findByProfessorId(Long professorId);

    @Query("""
    SELECT NEW com.example.demo.domain.prioridade.dto.DisciplinaContagemDTO(
        d.id,
        d.nome,
        COUNT(DISTINCT prof.id)
    )
    FROM Prioridade p
    JOIN p.matrizDisciplina md
    JOIN md.disciplina d
    JOIN p.professor prof
    GROUP BY d.id, d.nome
""")
    List<DisciplinaContagemDTO> contarProfessoresPorDisciplina();

//
//    @Query("""
//    SELECT p.professor
//    FROM Prioridade p
//    JOIN p.professor prof
//    JOIN p.matrizDisciplina md
//    WHERE prof.ativo = true
//      AND p.prioridade = :nivel
//      AND md.id = :matrizDisciplinaId
//""")
//    List<Professor> findProfessoresByMatrizDisciplinaAndNivelInteresse(
//            @Param("matrizDisciplinaId") Long matrizDisciplinaId,
//            @Param("nivel") PrioridadeNivel nivel
//    );



    List<Prioridade> findByMatrizDisciplina_Matriz_Curso_Escola_Ativo(boolean ativo);
}