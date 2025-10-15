package com.example.demo.service;



import com.example.demo.domain.horario.Turno;
import com.example.demo.domain.limiteTurno.LimiteTurno;
import com.example.demo.domain.limiteTurno.dto.LimiteTurnoCreateRequest;
import com.example.demo.domain.limiteTurno.dto.LimiteTurnoResponse;

import com.example.demo.domain.limiteTurno.dto.LimiteTurnoUpdateRequest;
import com.example.demo.infra.Exception.LimiteTurnoJaExiste;
import com.example.demo.infra.Exception.SobreposicaoHorarioException;
import com.example.demo.infra.Exception.TurnoNaoConfiguradoException;
import com.example.demo.infra.Exception.ValoresLimiteErrado;
import com.example.demo.repository.LimiteTurnoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalTime;

@Service
@Transactional
public class LimiteTurnoService {

    private final LimiteTurnoRepository limiteTurnoRepository;

    public LimiteTurnoService(LimiteTurnoRepository limiteTurnoRepository) {
        this.limiteTurnoRepository = limiteTurnoRepository;
    }

    @Transactional
    public LimiteTurnoResponse criar(LimiteTurnoCreateRequest request) {
        // Validação de unicidade do Turno
        if (limiteTurnoRepository.existsByTurno(request.turno())) {
            throw new LimiteTurnoJaExiste("O limite para o turno " + request.turno() + " já foi cadastrado.");
        }

        // Chamada da Validação de Tempo e Sobreposição
        verificarSobreposicao(request.limiteInicio(), request.limiteFim(), null, request.turno());

        LimiteTurno limite = new LimiteTurno(
                request.turno(),
                request.limiteInicio(),
                request.limiteFim()
        );
        return new LimiteTurnoResponse(limiteTurnoRepository.save(limite));
    }

    @Transactional
    public LimiteTurnoResponse atualizar(Long id, LimiteTurnoUpdateRequest request) {
        LimiteTurno limite = limiteTurnoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Limite de Turno não encontrado com ID: " + id));

        LocalTime novoInicio = request.limiteInicio() != null ? request.limiteInicio() : limite.getLimiteInicio();
        LocalTime novoFim = request.limiteFim() != null ? request.limiteFim() : limite.getLimiteFim();


        verificarSobreposicao(novoInicio, novoFim, id, limite.getTurno());

        limite.atualizar(novoInicio, novoFim);

        return new LimiteTurnoResponse(limiteTurnoRepository.save(limite));
    }

    @Transactional(readOnly = true)
    public List<LimiteTurnoResponse> buscarTodos() {
        return limiteTurnoRepository.findAll().stream()
            .map(LimiteTurnoResponse::new)
            .collect(Collectors.toList());
    }

    // Método crucial: usado pelo HorarioValidator para verificar coerência
    @Transactional(readOnly = true)
    public LimiteTurno buscarLimitePorTurno(Turno turno) {
        return limiteTurnoRepository.findByTurno(turno)
               .orElseThrow(() -> new TurnoNaoConfiguradoException("Limite de horário não configurado para o turno: " + turno + ". O Administrador deve configurá-lo primeiro."));
    }
    
    @Transactional
    public void deletar(Long id) {
        if (!limiteTurnoRepository.existsById(id)) {
            throw new EntityNotFoundException("Limite de Turno não encontrado com ID: " + id);
        }

        limiteTurnoRepository.deleteById(id);
    }
    private void verificarSobreposicao(LocalTime inicio, LocalTime fim, Long idExcluir, Turno turno) {


        if (inicio.isAfter(fim)) {
            throw new ValoresLimiteErrado("O limite de início deve ser anterior ao limite de fim.");
        }


        boolean sobrepoe;
        if (idExcluir == null) { // Caso CRIAR
            sobrepoe = limiteTurnoRepository.existsByLimiteInicioLessThanEqualAndLimiteFimGreaterThanEqual(
                    fim,
                    inicio
            );
        } else { // Caso ATUALIZAR
            sobrepoe = limiteTurnoRepository.existsByLimiteInicioLessThanEqualAndLimiteFimGreaterThanEqualAndIdNot(
                    fim,
                    inicio,
                    idExcluir
            );
        }

        if (sobrepoe) {
            throw new SobreposicaoHorarioException("O novo limite de horário (" + inicio + " - " + fim + ") se sobrepõe a um limite de turno já existente.");
        }
    }
}