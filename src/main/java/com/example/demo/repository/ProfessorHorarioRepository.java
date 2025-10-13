package com.example.demo.repository;

import com.example.demo.domain.horario.Horario;
import com.example.demo.domain.professor.Professor;

import com.example.demo.domain.professorHorario.ProfessorHorario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfessorHorarioRepository extends JpaRepository<ProfessorHorario, Long> {

    // Usado no POST e PATCH para evitar duplicidade
    Optional<ProfessorHorario> findByProfessorAndHorario(Professor professor, Horario horario);

    // MUDANÇA: Usado no buscarTodosAtivos() - Filtra APENAS pelo Professor Ativo
    List<ProfessorHorario> findByProfessor_Ativo(boolean ativo);
}