package com.example.demo.domain.professorHorario;

import com.example.demo.domain.horario.Horario;
import com.example.demo.domain.professor.Professor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Entity
@Table(name = "professor_horario",
    uniqueConstraints = @UniqueConstraint(columnNames = {"professor_id", "horario_id"})
)
@Data
@NoArgsConstructor

public class ProfessorHorario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "professor_id")
    private Professor professor;


    @ManyToOne
    @JoinColumn(name = "horario_id")
    private Horario horario;


    @Column(name = "is_disponivel", nullable = false)
    private boolean disponivel = true;

    public ProfessorHorario( Horario horario, Professor professor) {
        if(professor == null) throw  new IllegalArgumentException("ProfessorHorario deve  ter um professor");
        if(horario == null) throw  new IllegalArgumentException("ProfessorHorario deve possuir um  um horário");
        this.professor = professor;
        this.setHorario(horario);
        this.disponivel = true;
    }

    public  boolean isDisponivel (){
        return  this.disponivel;
    }


    public  void indisponibilizar(){
        this.disponivel = false;
    }
}