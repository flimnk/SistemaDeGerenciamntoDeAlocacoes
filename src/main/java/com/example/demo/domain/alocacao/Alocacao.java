package com.example.demo.domain.alocacao;

import com.example.demo.domain.horario.Horario;
import com.example.demo.domain.matrizDisciplina.MatrizDisciplina;
import com.example.demo.domain.professor.Professor;
import com.example.demo.domain.turma.Turma;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(
        name = "alocacao",
        uniqueConstraints = @UniqueConstraint(columnNames = {"turma_id", "matriz_disciplina_id", "horario_id"})
)

public class Alocacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    @ManyToOne
    @JoinColumn(name = "matriz_disciplina_id", nullable = false)
    private MatrizDisciplina matrizDisciplina;

    @ManyToOne
    @JoinColumn(name = "horario_id", nullable = false)
    private Horario horario;

    @ManyToOne
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;


    public Alocacao(Professor professor, MatrizDisciplina matrizDisciplina, Horario horario, Turma turma) {

        if (professor == null) throw new IllegalArgumentException("A Alocação deve ter um professor.");
        if (matrizDisciplina == null) throw new IllegalArgumentException("A Alocação deve ter uma Matriz Disciplina (contexto).");
        if (horario == null) throw new IllegalArgumentException("A Alocação deve ter um horário definido.");
        if (turma == null) throw new IllegalArgumentException("A Alocação deve estar vinculada a uma turma.");


        this.professor = professor;
        this.matrizDisciplina = matrizDisciplina;
        this.horario = horario;
        this.turma = turma;
    }


}