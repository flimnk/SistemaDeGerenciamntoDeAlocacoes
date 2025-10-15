package com.example.demo.repository;

import com.example.demo.domain.alocacao.Alocacao;
import com.example.demo.domain.horario.Horario;
import com.example.demo.domain.matrizDisciplina.MatrizDisciplina;
import com.example.demo.domain.professor.Professor;
import com.example.demo.domain.turma.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlocacaoRepository extends JpaRepository<Alocacao, Long> {
    Optional<Alocacao> findByProfessorAndHorario(Professor professor, Horario horario);
// AlocacaoRepository.java

    long countByMatrizDisciplina(MatrizDisciplina matrizDisciplina);

    Optional<Alocacao> findByTurmaAndHorario(Turma turma, Horario horario);
}