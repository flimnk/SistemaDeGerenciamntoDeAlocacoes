package com.example.demo.domain.professorHorario;

import com.example.demo.domain.horario.Horario;
import com.example.demo.domain.professor.Professor;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable; // Usado para a chave composta

@Entity
@Table(name = "professor_horario")
@Data
@NoArgsConstructor

public class ProfessorHorario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;


    @ManyToOne
    @JoinColumn(name = "horario_id")
    private Horario horario;
    

    @Column(name = "is_disponivel", nullable = false)
    private boolean disponivel = true; 

    public  void indiponibilizar(){
        this.disponivel = false;
    }

}