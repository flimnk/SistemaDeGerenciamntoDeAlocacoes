package com.example.demo.repository;

import com.example.demo.domain.professor.Professor;
import com.example.demo.domain.vo.Cpf;
import com.example.demo.domain.vo.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    boolean existsByCpf(Cpf cpf);

    boolean existsByEmail(Email email);

    boolean existsByRegistro(String registro);

    boolean existsByEmailAndIdNot(Email email, Long id);

    List<Professor> findAllByAtivoTrue();

    List<Professor> findByAtivoTrue();

    @Query("""
        SELECT p
        FROM Professor p
        JOIN p.disponibilidades ph
        JOIN p.escolas escola
        LEFT JOIN p.prioridades prioridade
            ON prioridade.matrizDisciplina.id = :matrizDisciplinaId
        WHERE p.ativo = true
        AND ph.horario.id = :horarioId
        AND ph.disponivel = true
        AND escola.id = (
            SELECT md.matriz.curso.escola.id 
            FROM MatrizDisciplina md WHERE md.id = :matrizDisciplinaId
        )
        ORDER BY prioridade.prioridadeNivel DESC NULLS LAST, p.nome ASC
    """)
    List<Professor> findElegiveisByMatrizDisciplinaAndHorario(
            @Param("matrizDisciplinaId") Long matrizDisciplinaId,
            @Param("horarioId") Long horarioId
    );
}