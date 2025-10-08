package com.example.demo.service;

import com.example.demo.domain.horario.Horario;
import com.example.demo.domain.horario.dto.HorarioCreateRequest;


import com.example.demo.domain.horario.dto.HorarioResponse;
import com.example.demo.domain.horario.dto.HorarioUpdateRequest;
import com.example.demo.infra.Exception.HorarioJaExisteException;
import com.example.demo.repository.HorarioRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HorarioService {
    private final HorarioRepository horarioRepository;

    public HorarioService ( HorarioRepository horarioRepository){
        this.horarioRepository = horarioRepository;
    }

    @Transactional
    public Horario criar(HorarioCreateRequest request) {
        System.out.println(request);
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

        horario.atualizar(request.diaSemana(), request.turno(), request.horarioInicio(), request.horarioFinal());

        if ( horarioRepository.existsByDiaSemanaAndTurnoAndHorarioInicioAndHorarioFimAndIdNot(
                request.diaSemana(),
                request.turno(),
                request.horarioInicio(),
                request.horarioFinal(),
                id)){
            throw new HorarioJaExisteException(request.toString());
        }

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