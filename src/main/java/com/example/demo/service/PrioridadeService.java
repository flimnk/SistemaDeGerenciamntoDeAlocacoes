package com.example.demo.service;


import com.example.demo.domain.escola.CategoriaEscola;
import com.example.demo.domain.matrizDisciplina.MatrizDisciplina;
import com.example.demo.domain.matrizDisciplina.dto.MatrizDisciplinaResponseSimples;
import com.example.demo.domain.prioridade.Prioridade;
import com.example.demo.domain.prioridade.PrioridadeNivel;
import com.example.demo.domain.prioridade.dto.*;
import com.example.demo.domain.professor.Professor;
import com.example.demo.domain.professor.dto.ProfessorResponse;
import com.example.demo.infra.Exception.DisciplinaInativaException;
import com.example.demo.infra.Exception.EscolaInativaExeption;
import com.example.demo.infra.Exception.PrioridadeJaExiste;
import com.example.demo.infra.Exception.ProfessorInativoException;
import com.example.demo.repository.MatrizDisciplinaRepository;
import com.example.demo.repository.PrioridadeRepository;
import com.example.demo.repository.ProfessorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrioridadeService {

    private final PrioridadeRepository prioridadeRepository;
    private final MatrizDisciplinaRepository mdRepository;
    private final ProfessorRepository professorRepository;
    private final SecurityService securityService;
    // ======================================================
    // 🔹 CREATE
    // ======================================================
    @Transactional
    public PrioridadeResponse criar(PrioridadeCreateRequest request) {
        Long professorId = getAuthenticatedProfessorId();
        MatrizDisciplina md = buscarMatrizDisciplinaEValidarAtividade(request.matrizDisciplinaId());
        Professor professor = buscarProfessorEValidarAtividade(professorId);

        verificaAtividadeEscola(md);
        verificaDuplicidade(md, professor, null);

        Prioridade prioridade = new Prioridade(md, request.prioridade(), professor);
        prioridade = prioridadeRepository.save(prioridade);
        return new PrioridadeResponse(prioridade);
    }
    // ======================================================
    // 🔹 UPDATE
    // ======================================================
    @Transactional
    public PrioridadeResponse atualizar(Long id, PrioridadeUpdateRequest request) {
        Long professorAutenticadoId = getAuthenticatedProfessorId();

        Prioridade prioridade = prioridadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prioridade não encontrada com ID: " + id));


        if (!prioridade.getProfessor().getId().equals(professorAutenticadoId)) {
            throw new AccessDeniedException("Você não tem permissão para alterar a prioridade de outro professor.");
        }

        MatrizDisciplina novaMd = prioridade.getMatrizDisciplina();
        Professor professorDaPrioridade = prioridade.getProfessor();
        boolean mudancaRelevante = false;

        if (request.matrizDisciplinaId() != null && !request.matrizDisciplinaId().equals(novaMd.getId())) {
            novaMd = buscarMatrizDisciplinaEValidarAtividade(request.matrizDisciplinaId());
            verificaAtividadeEscola(novaMd);
            mudancaRelevante = true;
        }

        if (mudancaRelevante) {

            verificaDuplicidade(novaMd, professorDaPrioridade, id);
        }

        prioridade.setMatrizDisciplina(novaMd);
        Optional.ofNullable(request.prioridade()).ifPresent(prioridade::setPrioridadeNivel);

        prioridade = prioridadeRepository.save(prioridade);
        return new PrioridadeResponse(prioridade);
    }

    // ======================================================
    // 🔹 READ
    // ======================================================
    @Transactional(readOnly = true)
    public PrioridadeResponse buscarPorId(Long id) {
        Prioridade prioridade = prioridadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prioridade não encontrada com ID: " + id));

        // Validações antes de retornar
        MatrizDisciplina md = prioridade.getMatrizDisciplina();
        verificaAtividadeEscola(md);
        verificaDisciplinaAtiva(md);

        return new PrioridadeResponse(prioridade);
    }
    @Transactional(readOnly = true)
    public List<PrioridadeResponse> buscarMinhasPrioridades() {
        // 1. Obtém o ID do professor do contexto de segurança
        Long professorId = getAuthenticatedProfessorId();

        // 2. Busca todas as prioridades associadas a esse professor
        List<Prioridade> minhasPrioridades = prioridadeRepository.findByProfessorId(professorId);

        // 3. Mapeia para o DTO de resposta
        return minhasPrioridades.stream()
                .map(PrioridadeResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PrioridadeResponse> buscarTodos() {
        return prioridadeRepository.findByMatrizDisciplina_Matriz_Curso_Escola_Ativo(true).stream()
                .filter(p -> p.getMatrizDisciplina().getDisciplina().isAtivo()) // filtra disciplinas ativas
                .map(PrioridadeResponse::new)
                .toList();
    }

//     ======================================================
//     🔹 REPORT (Disciplinas com interesse)
//     ======================================================


    public List<DisciplinaInteresseResponse> listarDisciplinasComDetalhesEContagem() {
        List<DisciplinaContagemDTO> contagens = prioridadeRepository.contarProfessoresPorDisciplina();
        contagens.forEach(System.out::println);
        List<Long>  mdsIds = contagens.stream().map(DisciplinaContagemDTO::matrizDisciplinaId).toList();
        List <DisciplinaInteresseResponse> disciplinaInteresseResponses = new ArrayList<>();
        var mds = mdsIds.stream().map(this::buscarMatrizDisciplinaEValidarAtividade)
                    .toList();
        Map<Long, MatrizDisciplina> detalhesMap = mds.stream()
                .collect(Collectors.toMap(MatrizDisciplina::getId, md -> md));


        return contagens.stream()
                .map(proj -> {
                    MatrizDisciplina md = detalhesMap.get(proj.matrizDisciplinaId());

                    MatrizDisciplinaResponseSimples responseSimples = new MatrizDisciplinaResponseSimples(md);

                    return new DisciplinaInteresseResponse(
                            responseSimples,
                            proj.quantidadeProfessoresInteressados()

                    );
                })
                .collect(Collectors.toList());



    }
    // ======================================================
    // 🔹 DELETE
    // ======================================================
    @Transactional
    public void deletar(Long id) {
        Prioridade prioridade = prioridadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prioridade não encontrada com ID: " + id));

        MatrizDisciplina md = prioridade.getMatrizDisciplina();
        verificaAtividadeEscola(md);
        verificaDisciplinaAtiva(md);

        prioridadeRepository.delete(prioridade);
    }

    // ======================================================
    // 🔹 MÉTODOS AUXILIARES DE VALIDAÇÃO
    // ======================================================
    private Long getAuthenticatedProfessorId() {
        Long professorId = securityService.getAuthenticatedProfessorId();

        if (professorId == null) {
            // Este é um ponto crucial: se o usuário está logado, mas NÃO é um Professor,
            // ou o objeto Professor não foi carregado corretamente, lançamos um erro.
            throw new IllegalStateException("O usuário autenticado não está associado a um Professor ou a sessão de segurança está incompleta.");
        }
        return professorId;
    }
    private MatrizDisciplina buscarMatrizDisciplinaEValidarAtividade(Long id) {
        var md = mdRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Matriz Disciplina não encontrada com ID: " + id));

        verificaDisciplinaAtiva(md);
        return md;
    }

    private void verificaDisciplinaAtiva(MatrizDisciplina md) {
        if (!md.getDisciplina().isAtivo()) {
            throw new DisciplinaInativaException(
                    "Disciplina inativa: '%s' (ID: %d)"
                            .formatted(md.getDisciplina().getNome(), md.getDisciplina().getId())
            );
        }
    }

    private Professor buscarProfessorEValidarAtividade(Long id) {
        var professor = professorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado com ID: " + id));

        if (!professor.isAtivo()) {
            throw new ProfessorInativoException("Professor inativo com ID: " + professor.getId());
        }

        return professor;
    }

    private void verificaAtividadeEscola(MatrizDisciplina md) {
        var escola = md.getMatriz().getCurso().getEscola();
        if (!escola.isAtivo()) {
            CategoriaEscola categoria = escola.getCategoriaEscola();
            String matrizNome = md.getMatriz().getNome();
            throw new EscolaInativaExeption(
                    "Operação bloqueada: A matriz '%s' pertence à escola inativa: %s."
                            .formatted(matrizNome, categoria)
            );
        }
    }

    private void verificaDuplicidade(MatrizDisciplina md, Professor pr, Long id) {
        prioridadeRepository.findByMatrizDisciplinaAndProfessor(md, pr)
                .filter(p -> p.getId() == null || !p.getId().equals(id))
                .ifPresent(p -> {
                    throw new PrioridadeJaExiste(
                            "Já existe uma prioridade definida para esta combinação de Professor e Matriz Disciplina."
                    );
                });
    }
}
