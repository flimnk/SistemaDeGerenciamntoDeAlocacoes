package com.example.demo.domain.horario;

import com.example.demo.domain.alocacao.Alocacao;
import com.example.demo.domain.professor.Professor;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "horario")
@Data
@NoArgsConstructor
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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


    @ManyToMany(mappedBy = "horarios",fetch = FetchType.LAZY)
    private Set<Professor> professores = new HashSet<>();



    public Horario(DiaSemana diaSemana, Turno turno, LocalTime horarioInicio, LocalTime horarioFim) {
        if (diaSemana == null) throw new IllegalArgumentException("O dia da semana é obrigatório.");
        if (turno == null) throw new IllegalArgumentException("O turno é obrigatório.");
        if (horarioInicio == null) throw new IllegalArgumentException("O horário de início é obrigatório.");
        if (horarioFim == null) throw new IllegalArgumentException("O horário de fim é obrigatório.");


        if (!horarioInicio.isBefore(horarioFim)) {
            throw new IllegalArgumentException("O horário de início deve ser anterior ao horário de fim.");
        }


        this.diaSemana = diaSemana;
        this.turno = turno;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
        this.alocacoes = new HashSet<>(); // Garante que a coleção seja inicializada
    }
    // Adicione à classe Horario:
    public void adicionarAlocacao(Alocacao alocacao) {
        this.alocacoes.add(alocacao);
        alocacao.setHorario(this); // Assume que Alocacao tem o método setHorario()
    }

    public void removerAlocacao(Alocacao alocacao) {
        this.alocacoes.remove(alocacao);
        if (alocacao.getHorario() != null && alocacao.getHorario().equals(this)) {
            alocacao.setHorario(null);
        }
    }

    public void adicionarProfessor(Professor professor) {
        this.professores.add(professor);
        professor.getHorarios().add(this);
    }

    public void removerProfessor(Professor professor) {
        this.professores.remove(professor);
        professor.getHorarios().remove(this);
    }

}