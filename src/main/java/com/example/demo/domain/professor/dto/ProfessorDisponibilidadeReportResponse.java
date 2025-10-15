package com.example.demo.domain.professor.dto;// package com.example.demo.domain.professor.dto;


import com.example.demo.domain.prioridade.dto.PrioridadeResponseSimples;

import com.example.demo.domain.professorHorario.dto.ProfessorHorarioResponse;
import com.example.demo.domain.professorHorario.dto.ProfessorHorarioResponseSimples;

import java.util.List;

// Este é o DTO que irá conter o resultado final para o administrador
public record ProfessorDisponibilidadeReportResponse(
    Long professorId,
    String nomeProfessor,
    String emailProfessor,
    boolean ativo,
    
    // Lista de horários que o professor marcou como "disponível"
    List<ProfessorHorarioResponseSimples> horariosDisponiveis,
    
    // Lista de disciplinas que o professor tem interesse e a prioridade
    List<PrioridadeResponseSimples> disciplinasInteresse
) {}