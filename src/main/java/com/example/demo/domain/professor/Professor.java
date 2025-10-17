package com.example.demo.domain.professor;

import com.example.demo.domain.alocacao.Alocacao;
import com.example.demo.domain.escola.Escola;
import com.example.demo.domain.formacao.Formacao; // Importe a classe Formacao
import com.example.demo.domain.interfaces.Ativavel;

import com.example.demo.domain.pessoa.Pessoa;
import com.example.demo.domain.prioridade.Prioridade;
import com.example.demo.domain.professorHorario.ProfessorHorario;
import com.example.demo.domain.user.User;
import com.example.demo.domain.vo.Cpf;
import com.example.demo.domain.vo.Email;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "professor")

public class Professor extends Pessoa implements Ativavel {


    @Column(name ="registro",nullable = false, unique = true)
    private String registro;



    @OneToOne(mappedBy = "professor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Formacao formacao;


    @ManyToMany
    @JoinTable(
            name = "professor_escola", // Corrigido o erro de digitação
            joinColumns = @JoinColumn(name = "professor_id"),
            inverseJoinColumns = @JoinColumn(name = "escola_id")
    )
    private Set<Escola> escolas = new HashSet<>();

    @OneToMany(mappedBy = "professor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProfessorHorario> disponibilidades = new HashSet<>();

    @OneToMany(mappedBy = "professor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Alocacao> alocacoes = new HashSet<>();


    @OneToMany(mappedBy = "professor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Prioridade> prioridades = new HashSet<>();

    @OneToOne(mappedBy = "professor", fetch = FetchType.LAZY)
    private User user;


    @Column(name = "is_ativo", nullable = false)
    private boolean ativo = true;

    public Professor(
            String nome,
            Cpf cpf,
            Email email,
            String registro


    ) {
        super(nome, email , cpf);
        if (registro == null || registro.isBlank()) {
            throw new IllegalArgumentException("Registro não pode ser nulo ou vazio");
        }

        this.registro = registro;
        this.ativo = true;

    }
    public void adicionarEscola(Escola escola) {
        if (!this.escolas.contains(escola)) {
            this.escolas.add(escola);

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
        // O correto seria verificar se prioridade.getProfessor() é igual a 'this'
        if (prioridade.getProfessor() != null && prioridade.getProfessor().equals(this)) {
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
    public void setFormacao(Formacao formacao) {
        this.formacao = formacao;
        if (formacao != null && formacao.getProfessor() != this) {
            formacao.setProfessor(this);
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

    public void atualizar(Formacao formacao, String nome, String email, Set<Escola> novasEscolas) {
        this.setEmail(email != null ? new Email(email) : this.getEmail());
        this.formacao = formacao != null ? formacao : this.formacao;
        this.setNome(nome != null ? nome : this.getNome());


        if (novasEscolas != null) {
            this.escolas.forEach(escola -> escola.getProfessores().remove(this));
            this.escolas.clear();


            novasEscolas.forEach(escola -> {
                this.escolas.add(escola);
                escola.getProfessores().add(this);
            });
        }
    }


}