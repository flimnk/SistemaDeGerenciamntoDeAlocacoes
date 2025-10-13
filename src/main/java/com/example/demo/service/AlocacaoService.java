package com.example.demo.service;

// ... (imports existentes) ...

import com.example.demo.domain.alocacao.Alocacao;
import com.example.demo.domain.alocacao.dto.AlocacaoCreateRequest;
import com.example.demo.domain.alocacao.dto.AlocacaoResponse;
import com.example.demo.domain.alocacao.dto.AlocacaoUpdateRequest; // NOVO
import com.example.demo.domain.horario.Horario;
import com.example.demo.domain.matriz_disciplina.MatrizDisciplina;
import com.example.demo.domain.professor.Professor;

import com.example.demo.domain.professorHorario.ProfessorHorario;
import com.example.demo.domain.turma.Turma;

import com.example.demo.infra.Exception.AlocacaoJaExisteException;
import com.example.demo.infra.Exception.EscolaInativaExeption;

import com.example.demo.infra.Exception.ProfessorInativoException;
import com.example.demo.infra.Exception.ProfessorIndisponivelException;
import com.example.demo.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlocacaoService {

    private final AlocacaoRepository alocacaoRepository;
    private final ProfessorRepository professorRepository;
    private final MatrizDisciplinaRepository mdRepository;
    private final HorarioRepository horarioRepository;
    private final TurmaRepository turmaRepository;
    private final ProfessorHorarioRepository professorHorarioRepository;



    @Transactional
    public AlocacaoResponse criar(AlocacaoCreateRequest request) {
        Professor professor = buscarProfessorEValidarAtividade(request.professorId());
        MatrizDisciplina md = buscarMatrizDisciplinaEValidarAtividadeEscola(request.matrizDisciplinaId());
        Horario horario = buscarHorario(request.horarioId());
        Turma turma = buscarTurmaEValidarAtividadeEscola(request.turmaId());

        verificarDisponibilidadeProfessor(professor, horario);
        verificarConflitosDeAlocacao(professor, horario, turma, null);

        Alocacao alocacao = new Alocacao(professor, md, horario, turma);
        alocacao = alocacaoRepository.save(alocacao);
        return new AlocacaoResponse(alocacao);
    }
    
    // --- BUSCA POR ID (GET) ---
    @Transactional(readOnly = true)
    public AlocacaoResponse buscarPorId(Long id) {
        Alocacao alocacao = alocacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Alocação não encontrada com ID: " + id));
        
        // Regra de Inatividade: Bloqueia a leitura se Professor ou Escola estiverem inativos
        validarAtividadeGeral(alocacao);
        
        return new AlocacaoResponse(alocacao);
    }

    @Transactional(readOnly = true)
    public List<AlocacaoResponse> buscarTodas() {

        
        return alocacaoRepository.findAll().stream()
                .filter(a -> a.getProfessor().isAtivo() && a.getMatrizDisciplina().getMatriz().getCurso().getEscola().isAtivo())
                .map(AlocacaoResponse::new)
                .toList();
    }
    

    @Transactional
    public AlocacaoResponse atualizar(Long id, AlocacaoUpdateRequest request) {
        Alocacao alocacao = alocacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Alocação não encontrada com ID: " + id));
        

        validarAtividadeGeral(alocacao);
        
        Professor novoProfessor = alocacao.getProfessor();
        MatrizDisciplina novaMd = alocacao.getMatrizDisciplina();
        Horario novoHorario = alocacao.getHorario();
        Turma novaTurma = alocacao.getTurma();
        boolean houveMudancaRelevante = false;

        // 1. Atualizar Professor
        if (request.professorId() != null && !request.professorId().equals(novoProfessor.getId())) {
            novoProfessor = buscarProfessorEValidarAtividade(request.professorId());
            alocacao.setProfessor(novoProfessor);
            houveMudancaRelevante = true;
        }


        if (request.matrizDisciplinaId() != null && !request.matrizDisciplinaId().equals(novaMd.getId())) {
            novaMd = buscarMatrizDisciplinaEValidarAtividadeEscola(request.matrizDisciplinaId());
            alocacao.setMatrizDisciplina(novaMd);
        }


        if (request.horarioId() != null && !request.horarioId().equals(novoHorario.getId())) {
            novoHorario = buscarHorario(request.horarioId());
            alocacao.setHorario(novoHorario);
            houveMudancaRelevante = true;
        }


        if (request.turmaId() != null && !request.turmaId().equals(novaTurma.getId())) {
            novaTurma = buscarTurmaEValidarAtividadeEscola(request.turmaId());
            alocacao.setTurma(novaTurma);
        }

        if (houveMudancaRelevante) {
            verificarDisponibilidadeProfessor(alocacao.getProfessor(), alocacao.getHorario());
            verificarConflitosDeAlocacao(alocacao.getProfessor(), alocacao.getHorario(), alocacao.getTurma(), id);
        }

        alocacao = alocacaoRepository.save(alocacao);
        return new AlocacaoResponse(alocacao);
    }


    @Transactional
    public void deletar(Long id) {
        Alocacao alocacao = alocacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Alocação não encontrada com ID: " + id));


        validarAtividadeGeral(alocacao);

        alocacaoRepository.delete(alocacao);
    }
    

    
    private void validarAtividadeGeral(Alocacao alocacao) {
        // Alocação é inativa se o Professor não está ativo
        if (!alocacao.getProfessor().isAtivo()) {
            throw new ProfessorInativoException("Operação bloqueada: Professor da alocação inativo.");
        }
        // Alocação é inativa se a Escola associada (via MatrizDisciplina ou Turma) não está ativa
        if (!alocacao.getMatrizDisciplina().getMatriz().getCurso().getEscola().isAtivo()) {
            throw new EscolaInativaExeption("Operação bloqueada: Escola da Alocação inativa.");
        }
    }

    // VALIDAÇÃO 1: Disponibilidade do Professor no Horário (usando ProfessorHorario)
    private void verificarDisponibilidadeProfessor(Professor professor, Horario horario) {
        ProfessorHorario ph = professorHorarioRepository.findByProfessorAndHorario(professor, horario)
                .orElseThrow(() -> new ProfessorIndisponivelException(
                        "O Professor não tem um horário registrado (ProfessorHorario) para o horário fornecido."
                ));

        if (!ph.isDisponivel()) {
            throw new ProfessorIndisponivelException(
                    "O Professor está registrado como indisponível (ProfessorHorario.disponivel=false) para o horário."
            );
        }
    }

    // VALIDAÇÃO 2: Conflitos na Alocação (Duplicidade)
    private void verificarConflitosDeAlocacao(Professor professor, Horario horario, Turma turma, Long idAtual) {

        // Conflito A: Professor já alocado naquele horário? (Professor só pode dar 1 aula por horário)
        alocacaoRepository.findByProfessorAndHorario(professor, horario)
                .filter(a -> !a.getId().equals(idAtual))
                .ifPresent(a -> {
                    throw new AlocacaoJaExisteException(
                            "Conflito: O Professor já está alocado em outra aula neste horário."
                    );
                });

        // Conflito B: Turma já alocada naquele horário? (Turma só pode ter 1 aula por horário)
        alocacaoRepository.findByTurmaAndHorario(turma, horario)
                .filter(a -> !a.getId().equals(idAtual))
                .ifPresent(a -> {
                    throw new AlocacaoJaExisteException(
                            "Conflito: A Turma já tem uma aula alocada neste horário."
                    );
                });
    }

    // MÉTODOS DE BUSCA E VALIDAÇÃO DE ATIVIDADE (Reutilizando lógica anterior)

    // Professor (apenas ativo)
    private Professor buscarProfessorEValidarAtividade(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado com ID: " + id));
        if (!professor.isAtivo()) {
            throw new IllegalArgumentException("Professor inativo.");
        }
        return professor;
    }

    // MatrizDisciplina (valida a Escola)
    private MatrizDisciplina buscarMatrizDisciplinaEValidarAtividadeEscola(Long id) {
        MatrizDisciplina md = mdRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Matriz Disciplina não encontrada com ID: " + id));
        if (!md.getMatriz().getCurso().getEscola().isAtivo()) { // Assume MD -> Curso -> Escola
            throw new EscolaInativaExeption("Escola do Curso da Matriz Disciplina inativa.");
        }
        return md;
    }

    // Turma (valida a Escola, pois Turma -> Curso -> Escola)
    private Turma buscarTurmaEValidarAtividadeEscola(Long id) {
        Turma turma = turmaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada com ID: " + id));
        if (!turma.getCurso().getEscola().isAtivo()) {
            throw new EscolaInativaExeption("Escola do Curso da Turma inativa.");
        }
        return turma;
    }

    // Horario (apenas busca, pois não tem ativo)
    private Horario buscarHorario(Long id) {
        return horarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Horário não encontrado com ID: " + id));
    }
}
