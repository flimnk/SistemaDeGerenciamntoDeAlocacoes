package com.example.demo.service;

import com.example.demo.domain.horario.DiaSemana;
import com.example.demo.domain.horario.Horario;
import com.example.demo.domain.horario.Turno;
import com.example.demo.domain.horario.dto.HorarioCreateRequest;


import com.example.demo.domain.horario.dto.HorarioResponse;
import com.example.demo.domain.horario.dto.HorarioUpdateRequest;
import com.example.demo.infra.Exception.HorarioJaExisteException;
import com.example.demo.repository.HorarioRepository;

import com.example.demo.service.validation.HorarioValidator;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HorarioService {
    private final HorarioRepository horarioRepository;
    private final HorarioValidator horarioValidator; // Injetar o validador

    public HorarioService ( HorarioRepository horarioRepository, HorarioValidator horarioValidator){
        this.horarioRepository = horarioRepository;
        this.horarioValidator = horarioValidator; // Atribuir
    }

    @Transactional
    public Horario criar(HorarioCreateRequest request) {
;
        // 1. Chamar a validação ANTES de verificar a duplicidade e salvar
        horarioValidator.validar(
                request.diaSemana(),
                request.turno(),
                request.horarioInicio(),
                request.horarioFinal()
        );

        if (horarioRepository.existsByDiaSemanaAndTurnoAndHorarioInicioAndHorarioFim(
                request.diaSemana(),
                request.turno(),
                request.horarioInicio(),
                request.horarioFinal()
        )) {
            throw new HorarioJaExisteException(request.toString());
        }

        Horario horario = new Horario(
                request.diaSemana(),
                request.turno(),
                request.horarioInicio(),
                request.horarioFinal()
        );

        return horarioRepository.save(horario);
    }


    @Transactional
    public Horario atualizar(Long id, HorarioUpdateRequest request) {
        Horario horario = horarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Horário não encontrado com id " + id));


        DiaSemana novoDia = request.diaSemana() != null ? request.diaSemana() : horario.getDiaSemana();
        Turno novoTurno = request.turno() != null ? request.turno() : horario.getTurno();
        LocalTime novoInicio = request.horarioInicio() != null ? request.horarioInicio() : horario.getHorarioInicio();
        LocalTime novoFim = request.horarioFinal() != null ? request.horarioFinal() : horario.getHorarioFim();


        horarioValidator.validar(novoDia, novoTurno, novoInicio, novoFim);

        if ( horarioRepository.existsByDiaSemanaAndTurnoAndHorarioInicioAndHorarioFimAndIdNot(
                novoDia, novoTurno, novoInicio, novoFim, id)){
            throw new HorarioJaExisteException(request.toString());
        }
        horario.atualizar(request.diaSemana(), request.turno(), request.horarioInicio(), request.horarioFinal());




        return horarioRepository.save(horario);
    }

    @Transactional
    public void deletar(Long id) {
        Horario horario = horarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Horário não encontrado com id " + id));
        horarioRepository.delete(horario);
    }

    @Transactional(readOnly = true)
    public HorarioResponse buscarPorId(Long id) {
       var horario = horarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Horário não encontrado com id " + id));
       return new HorarioResponse(horario);
    }


    @Transactional(readOnly = true)
    public List<HorarioResponse> buscarTodos() {
        return horarioRepository.findAll()
                .stream()
                .map(HorarioResponse::new)
                .collect(Collectors.toList());
    }
}