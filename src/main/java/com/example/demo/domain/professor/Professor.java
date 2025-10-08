package com.example.demo.domain.professor;

import com.example.demo.domain.alocacao.Alocacao;
import com.example.demo.domain.escola.Escola;
import com.example.demo.domain.formacao.Formacao; // Importe a classe Formacao
import com.example.demo.domain.horario.Horario;
import com.example.demo.domain.interfaces.Ativavel;

import com.example.demo.domain.pessoa.Pessoa;
import com.example.demo.domain.prioridade.Prioridade;
import com.example.demo.domain.professorHorario.ProfessorHorario;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "professor")

public class Professor extends Pessoa implements Ativavel {



    @ManyToMany
    @JoinTable(
            name = "professor_escola", // Corrigido o erro de digitação
            joinColumns = @JoinColumn(name = "professor_id"),
            inverseJoinColumns = @JoinColumn(name = "escola_id")
    )
    private Set<Escola> escolas = new HashSet<>();

    @OneToMany(mappedBy = "professor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProfessorHorario> disponibilidades = new HashSet<>();

    @Column(nullable = false, unique = true)
    private String registro;

    @OneToMany(mappedBy = "professor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Formacao> formacoes = new HashSet<>();

    @OneToMany(mappedBy = "professor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Alocacao> alocacoes = new HashSet<>();


    @OneToMany(mappedBy = "professor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Prioridade> prioridades = new HashSet<>();

    @Column(name = "is_ativo", nullable = false)
    private boolean ativo = true;


    public Professor(String registro, Set<Formacao> formacao) {
        if (registro == null || registro.isBlank()) throw new IllegalArgumentException("Registro não pode ser nulo ou vazio");
        if (formacao == null) throw new IllegalArgumentException("Formação é obrigatória");

        this.registro = registro;
        this.formacoes = formacao;
        this.ativo = true;
    }

    public void adicionarEscola(Escola escola) {
        this.escolas.add(escola);
        // Garante que o lado Escola (coleção de professores) também seja atualizado.
        if (!escola.getProfessores().contains(this)) {
            escola.getProfessores().add(this);
        }
    }


    public void removerEscola(Escola escola) {
        this.escolas.remove(escola);

        if (escola.getProfessores().contains(this)) {
            escola.getProfessores().remove(this);
        }
    }
    public void adicionarFormacao(Formacao formacao) {
        this.formacoes.add(formacao);

        if (formacao.getProfessor() != this) {
            formacao.setProfessor(this);
        }
    }
    public void removerFormacao(Formacao formacao) {
        this.formacoes.remove(formacao);

        if (formacao.getProfessor() != null && formacao.getProfessor().equals(this)) {
            formacao.setProfessor(null);
        }
    }
    public void removerAlocacao(Alocacao alocacao) {
        this.alocacoes.remove(alocacao);

        if (alocacao.getProfessor() != null && alocacao.getProfessor().equals(this)) {
            alocacao.setProfessor(null);
        }
    }
    public void adicionarAlocacao(Alocacao alocacao) {
        this.alocacoes.add(alocacao);

        if (alocacao.getProfessor() != this) {
            alocacao.setProfessor(this);
        }
    }
    public void adicionarPrioridade(Prioridade prioridade) {

        this.prioridades.add(prioridade);
        if (prioridade.getProfessor() != this) {
            prioridade.setProfessor(this);
        }
    }

    public void removerPrioridade(Prioridade prioridade) {
        this.prioridades.remove(prioridade);
        if (prioridade.getProfessor() != null && prioridade.getMatrizDisciplina().equals(this)) {
            prioridade.setProfessor(null);
        }
    }


    public void adicionarDisponibilidade(ProfessorHorario disponibilidade) {
        this.disponibilidades.add(disponibilidade);


        if (disponibilidade.getProfessor() != this) {
            disponibilidade.setProfessor(this);
        }
    }

    public void removerDisponibilidade(ProfessorHorario disponibilidade) {
        this.disponibilidades.remove(disponibilidade);

        if (disponibilidade.getProfessor() != null && disponibilidade.getProfessor().equals(this)) {
            disponibilidade.setProfessor(null);
        }
    }
    @Override
    public boolean isAtivo() {
        return ativo;
    }

    @Override
    public void ativar() {
        this.ativo = true;
    }

    @Override
    public void desativar() {
        this.ativo = false;
    }
}