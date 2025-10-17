package com.example.demo.domain.horario;

import com.example.demo.domain.alocacao.Alocacao;
import com.example.demo.domain.professorHorario.ProfessorHorario;
import com.example.demo.service.validation.HorarioValidator;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "horario",
        uniqueConstraints = @UniqueConstraint(columnNames = {"dia_semana", "turno", "horario_inicio", "horario_fim"})
)
@Getter
@Setter
@NoArgsConstructor
public class Horario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;


    @Column(name = "dia_semana", nullable = false)
    @Enumerated(EnumType.STRING)
    private DiaSemana diaSemana;

    @Column(name = "turno", nullable = false)
    @Enumerated(EnumType.STRING)
    private Turno turno;

    @Column(name = "horario_inicio", nullable = false)
    private LocalTime horarioInicio;

    @Column(name = "horario_fim", nullable = false)
    private LocalTime horarioFim;

    @OneToMany(mappedBy = "horario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Alocacao> alocacoes = new HashSet<>();


    @OneToMany(mappedBy = "horario", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProfessorHorario> disponibilidades = new HashSet<>();

    public Horario(DiaSemana diaSemana, Turno turno, LocalTime horarioInicio, LocalTime horarioFim) {
        this.diaSemana = diaSemana;
        this.turno = turno;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
        this.alocacoes = new HashSet<>();
    }

    public void atualizar(DiaSemana diaSemana, Turno turno, LocalTime horarioInicio, LocalTime horarioFim) {
        DiaSemana novoDia = diaSemana != null ? diaSemana : this.diaSemana;
        Turno novoTurno = turno != null ? turno : this.turno;
        LocalTime novoInicio = horarioInicio != null ? horarioInicio : this.horarioInicio;
        LocalTime novoFim = horarioFim != null ? horarioFim : this.horarioFim;


        this.diaSemana = novoDia;
        this.turno = novoTurno;
        this.horarioInicio = novoInicio;
        this.horarioFim = novoFim;
    }

    public void adicionarAlocacao(Alocacao alocacao) {
        this.alocacoes.add(alocacao);
        alocacao.setHorario(this);
    }

    public void removerAlocacao(Alocacao alocacao) {
        this.alocacoes.remove(alocacao);
        if (alocacao.getHorario() != null && alocacao.getHorario().equals(this)) {
            alocacao.setHorario(null);
        }
    }

    public void adicionarDisponibilidade(ProfessorHorario disponibilidade) {
        this.disponibilidades.add(disponibilidade);


        if (disponibilidade.getHorario() != this) {
            disponibilidade.setHorario(this);
        }
    }

    public void removerDisponibilidade(ProfessorHorario disponibilidade) {
        this.disponibilidades.remove(disponibilidade);


        if (disponibilidade.getHorario() != null && disponibilidade.getHorario().equals(this)) {
            disponibilidade.setHorario(null);
        }
    }



}