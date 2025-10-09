package com.example.demo.service;

import com.example.demo.domain.formacao.Formacao;
import com.example.demo.domain.formacao.dto.FormacaoCreateRequest;
import com.example.demo.domain.formacao.dto.FormacaoResponse;
import com.example.demo.domain.formacao.dto.FormacaoUpdateRequest;
import com.example.demo.domain.professor.Professor;

import com.example.demo.infra.Exception.ProfessorJaExisteException;
import com.example.demo.infra.Exception.ProfessorJaPossuiFormacaoException;
import com.example.demo.repository.FormacaoRepository;
import com.example.demo.repository.ProfessorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FormacaoService {

    private final FormacaoRepository formacaoRepository;
    private final ProfessorRepository professorRepository;

    public FormacaoService(FormacaoRepository formacaoRepository, ProfessorRepository professorRepository) {
        this.formacaoRepository = formacaoRepository;
        this.professorRepository = professorRepository;
    }

    @Transactional
    public FormacaoResponse criar(FormacaoCreateRequest request) {
        Professor professor = professorRepository.findById(request.professorId())
                .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado com id: " + request.professorId()));

        if(professor.getFormacao()!=null) throw  new ProfessorJaPossuiFormacaoException("Professor ja possui uma formacao: "+ professor.toString());

        Formacao formacao = new Formacao(
                request.nomeCurso(),
                request.anoConclusao(),
                request.nomeInstituicao(),
                request.categoria(),
                professor
        );

        formacaoRepository.save(formacao);
        return new FormacaoResponse(formacao);
    }

    @Transactional(readOnly = true)
    public List<FormacaoResponse> buscarTodos() {
        return formacaoRepository.findAll()
                .stream()
                .map(FormacaoResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public FormacaoResponse buscarPorId(Long id) {
        Formacao formacao = formacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Formação não encontrada com id: " + id));
        return new FormacaoResponse(formacao);
    }

    @Transactional
    public FormacaoResponse atualizar(Long id, FormacaoUpdateRequest request) {
        Formacao formacao = formacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Formação não encontrada com id: " + id));

        Professor professor = null;
        if (request.professorId() != null) {
            professor = professorRepository.findById(request.professorId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Professor não encontrado com id: " + request.professorId()));
        }


        formacao.atualizar(request, professor);

        return new FormacaoResponse(formacao);
    }

    @Transactional
    public void deletar(Long id) {
        Formacao formacao = formacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Formação não encontrada com id: " + id));
        formacaoRepository.delete(formacao);
    }
}
