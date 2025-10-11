package com.example.demo.service;

import com.example.demo.domain.escola.Escola;
import com.example.demo.domain.professor.Professor;
import com.example.demo.domain.professor.dto.*;
import com.example.demo.domain.formacao.Formacao;

import com.example.demo.domain.vo.Cpf;
import com.example.demo.domain.vo.Email;
import com.example.demo.infra.Exception.ProfessorInativoException;
import com.example.demo.infra.Exception.ProfessorJaExisteException;
import com.example.demo.repository.EscolaRepository;
import com.example.demo.repository.FormacaoRepository;
import com.example.demo.repository.ProfessorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final FormacaoRepository formacaoRepository;
    private final EscolaRepository escolaRepository;
    public ProfessorService(ProfessorRepository professorRepository, FormacaoRepository formacaoRepository, EscolaRepository escolaRepository) {
        this.professorRepository = professorRepository;
        this.formacaoRepository = formacaoRepository;
        this.escolaRepository = escolaRepository;
    }
    @Transactional
    public ProfessorResponse criar(ProfessorCreateRequest request) {
        verificaDuplicacao(new Cpf(request.cpf()),new Email(request.email()), request.registro());


        Professor professor = new Professor(
                request.nome(),
                new Cpf(request.cpf()),
                new Email(request.email()),
                request.registro()

        );
       var escolas =  request.escolasIds().stream()
               .map(escolaId -> escolaRepository
                       .findById(escolaId).orElseThrow(()-> new EntityNotFoundException("Escola não encontrada com id: " + escolaId)));
       escolas.forEach(professor::adicionarEscola);


        professorRepository.save(professor);

        return  new ProfessorResponse(professor);
    }
    @Transactional
    public ProfessorResponse atualizar(Long id, ProfessorUpdateRequest request) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado com id: " + id));

        if(!professor.isAtivo()) throw  new ProfessorInativoException("Professor inativo com id: " + professor.getId());

        if (request.email() != null &&
                professorRepository.existsByEmailAndIdNot(new Email(request.email()), id)) {
            throw new ProfessorJaExisteException("Já existe professor com email: " + request.email());
        }


        Formacao formacao = null;
        if (request.formacaoId() != null) {
            formacao = formacaoRepository.findById(request.formacaoId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Formação não encontrada com id: " + request.formacaoId()));
        }
        Set<Escola> novasEscolas = new HashSet<Escola>();
        if (request.escolasIds() != null && !request.escolasIds().isEmpty()) {
            novasEscolas = buscarEscolasAtivasOuFalhar(request.escolasIds());
        }


        professor.atualizar(formacao, request.nome(), request.email(), novasEscolas);


        professorRepository.save(professor);

        return new ProfessorResponse(professor);
    }
    public List<ProfessorResponse> buscarTodosAtivos() {
        return professorRepository.findAllByAtivoTrue().stream()
                .map(ProfessorResponse::new)
                .collect(Collectors.toList());
    }


    @Transactional
    public void deletar(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(" Professor não encontrado com id: " + id));

        professorRepository.delete(professor);
    }

    @Transactional(readOnly = true)
    public ProfessorResponse buscarPorId(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(" Professor não encontrado com id: " + id));
        return new ProfessorResponse(professor);
    }

    @Transactional(readOnly = true)
    public List<ProfessorResponse>buscarTodos() {
        return professorRepository.findAll().stream().map(ProfessorResponse::new)
                .collect(Collectors.toList());
    }


    @Transactional
    public void ativarProfessor(Long id ){
        var professor = professorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(" Professor não encontrado com id: " + id));
        professor.ativar();
        professorRepository.save(professor);
    }
    @Transactional
    public void desativarProfessor(Long id ){
        var professor = professorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(" Professor não encontrado com id: " + id));
        professor.desativar();
        professorRepository.save(professor);
    }



    public  void  verificaDuplicacao(Cpf cpf , Email email,String registro){

        if (professorRepository.existsByCpf(cpf)) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }

        if (professorRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        if (professorRepository.existsByRegistro(registro)) {
            throw new IllegalArgumentException("Registro já cadastrado");
        }
    }

    private Set<Escola> buscarEscolasAtivasOuFalhar(Set<Long> ids) {
        return ids.stream()
                .map(id -> {
                    Escola escola = escolaRepository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("Escola não encontrada com id: " + id));

                    if (!escola.isAtivo()) {
                        throw new IllegalStateException("Escola com id " + id + " está inativa");
                    }

                    return escola;
                })
                .collect(Collectors.toSet());
    }


}
