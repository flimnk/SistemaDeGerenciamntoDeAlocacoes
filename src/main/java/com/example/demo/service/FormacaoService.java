package com.example.demo.service;

import com.example.demo.domain.formacao.Formacao;
import com.example.demo.domain.formacao.dto.FormacaoCreateRequest;
import com.example.demo.domain.formacao.dto.FormacaoResponse;
import com.example.demo.domain.formacao.dto.FormacaoUpdateRequest;
import com.example.demo.domain.professor.Professor;

import com.example.demo.infra.Exception.ProfessoNaoPossuiFormacaoException;
import com.example.demo.infra.Exception.ProfessorInativoException;
import com.example.demo.infra.Exception.ProfessorJaExisteException;
import com.example.demo.infra.Exception.ProfessorJaPossuiFormacaoException;
import com.example.demo.repository.FormacaoRepository;
import com.example.demo.repository.ProfessorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class FormacaoService {

    private final FormacaoRepository formacaoRepository;
    private final ProfessorRepository professorRepository;
    private  final SecurityService securityService;



    @Transactional
    public FormacaoResponse criar(FormacaoCreateRequest request) {
        var professorId = securityService.getAuthenticatedProfessorId();
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado com id: " + professorId));

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
    @Transactional(readOnly = true)
    public FormacaoResponse buscarMinhasFormacoes() {
        var professor = buscaEVerificaProfessor();
        Formacao formacao = formacaoRepository.findByProfessor(professor)
                .orElseThrow(() -> new EntityNotFoundException("Formação não encontrada com id: " + professor));
        return new FormacaoResponse(formacao);
    }


    @Transactional
    public FormacaoResponse atualizar( FormacaoUpdateRequest request) {
        var professor = buscaEVerificaProfessor();
        if(professor.getFormacao() == null) throw  new ProfessoNaoPossuiFormacaoException("Professor não possui formação para ser atualizada");
        professor.getFormacao().atualizar(request);

        return new FormacaoResponse(professor.getFormacao());
    }

    @Transactional
    public void deletar(Long id) {
        var professor = buscaEVerificaProfessor();
        Formacao formacao = formacaoRepository.findByProfessor(professor)
                .orElseThrow(() -> new EntityNotFoundException("Formação não encontrada com id: " + id));
        formacaoRepository.delete(formacao);
    }


    public  Professor buscaEVerificaProfessor() {
        var professorId = securityService.getAuthenticatedProfessorId();
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado com id: " + professorId));
        if(!professor.isAtivo()) throw  new ProfessorInativoException("Professor inativo com id:"+professorId);
        return  professor;
    }
}
