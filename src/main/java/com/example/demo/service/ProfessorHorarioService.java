package com.example.demo.service;

import com.example.demo.domain.horario.Horario;
import com.example.demo.domain.professor.Professor;
import com.example.demo.domain.professorHorario.ProfessorHorario;
import com.example.demo.domain.professorHorario.dto.ProfessorHorarioCreateRequest;
import com.example.demo.domain.professorHorario.dto.ProfessorHorarioResponse;
import com.example.demo.domain.professorHorario.dto.ProfessorHorarioUpdateRequest;
import com.example.demo.infra.Exception.ProfessorHorarioJaExiste;
import com.example.demo.infra.Exception.ProfessorInativoException;
import com.example.demo.repository.HorarioRepository;
import com.example.demo.repository.ProfessorHorarioRepository;
import com.example.demo.repository.ProfessorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfessorHorarioService {

    private final ProfessorHorarioRepository phRepository;
    private final ProfessorRepository professorRepository;
    private final HorarioRepository horarioRepository;

    // --- CRIAÇÃO (POST) ---
    @Transactional
    public ProfessorHorarioResponse criar(ProfessorHorarioCreateRequest request) {

        Professor professor = buscarProfessorEValidarAtividade(request.professorId());
        Horario horario = buscarHorario(request.horarioId());
        verificaDuplicidade(professor, horario, null);

        ProfessorHorario ph = new ProfessorHorario(horario, professor);

        ph = phRepository.save(ph);
        return new ProfessorHorarioResponse(ph);
    }


    @Transactional
    public ProfessorHorarioResponse atualizar(Long id, ProfessorHorarioUpdateRequest request) {
        ProfessorHorario ph = phRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ProfessorHorario não encontrado com ID: " + id));

        Professor novoProfessor = ph.getProfessor();
        Horario novoHorario = ph.getHorario();
        boolean mudancaRelevante = false;


        if (request.professorId() != null && !request.professorId().equals(novoProfessor.getId())) {
            novoProfessor = buscarProfessorEValidarAtividade(request.professorId());
            mudancaRelevante = true;
        }


        if (request.horarioId() != null && !request.horarioId().equals(novoHorario.getId())) {
            novoHorario = buscarHorario(request.horarioId()); // Busca normal, sem validação de Escola
            mudancaRelevante = true;
        }


        if (mudancaRelevante) {
            verificaDuplicidade(novoProfessor, novoHorario, id);
        }


        ph.setProfessor(novoProfessor);
        ph.setHorario(novoHorario);
        Optional.ofNullable(request.disponivel()).ifPresent(ph::setDisponivel);

        ph = phRepository.save(ph);
        return new ProfessorHorarioResponse(ph);
    }


    @Transactional(readOnly = true)
    public ProfessorHorarioResponse buscarPorId(Long id) {
        ProfessorHorario ph = phRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ProfessorHorario não encontrado com ID: " + id));


        validarAtividadeProfessor(ph.getProfessor());

        return new ProfessorHorarioResponse(ph);
    }


    @Transactional(readOnly = true)
    public List<ProfessorHorarioResponse> buscarTodosAtivos() {

        return phRepository.findByProfessor_Ativo(true).stream()
                .map(ProfessorHorarioResponse::new)
                .toList();
    }

    // --- DELETAR ---
    @Transactional
    public void deletar(Long id) {
        ProfessorHorario ph = phRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ProfessorHorario não encontrado com ID: " + id));

        // Regra de inatividade: Bloqueia a deleção se o Professor estiver inativo.
        validarAtividadeProfessor(ph.getProfessor());

        phRepository.delete(ph);
    }

    // --- MÉTODOS AUXILIARES E VALIDAÇÕES ---

    private Horario buscarHorario(Long id) {
        return horarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Horário não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<ProfessorHorarioResponse> buscarTodosDisponiveis() {
        // Filtra por Professor ATIVO E ProfessorHorario.disponivel = TRUE
        return phRepository.findByProfessor_AtivoTrueAndDisponivelTrue().stream()
                .map(ProfessorHorarioResponse::new)
                .toList();
    }

    // MÉTODOS DE VALIDAÇÃO DE ATIVIDADE:

    private Professor buscarProfessorEValidarAtividade(Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado com ID: " + id));
        validarAtividadeProfessor(professor);
        return professor;
    }

    private void validarAtividadeProfessor(Professor professor) {
        if (!professor.isAtivo()) {
            throw new ProfessorInativoException("Operação bloqueada: Professor inativo com ID: " + professor.getId());
        }
    }



    private void verificaDuplicidade(Professor professor, Horario horario, Long id) {
        phRepository.findByProfessorAndHorario(professor, horario)
                .filter(existing -> existing.getId() == null || !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ProfessorHorarioJaExiste("Já existe um registro de horário para este Professor e Horário.");
                });
    }
}