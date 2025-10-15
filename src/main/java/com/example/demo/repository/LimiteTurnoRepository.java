package com.example.demo.repository;

import com.example.demo.domain.horario.Turno;

import com.example.demo.domain.limiteTurno.LimiteTurno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface LimiteTurnoRepository extends JpaRepository<LimiteTurno, Long> {


    boolean existsByTurno(Turno turno);


    Optional<LimiteTurno> findByTurno(Turno turno);
    /**
     * Verifica se existe um limite de turno (excluindo o ID atual) que se sobrepõe
     * ao novo intervalo [novoInicio, novoFim].
     *
     * A sobreposição ocorre se:
     * (A) O novo início está DENTRO de um limite existente. OU
     * (B) O novo fim está DENTRO de um limite existente. OU
     * (C) O novo limite ENGLOBA um limite existente.
     */
    boolean existsByLimiteInicioLessThanEqualAndLimiteFimGreaterThanEqualAndIdNot(
            LocalTime limiteFim,      // Novo fim é maior que o início existente
            LocalTime limiteInicio,   // Novo início é menor que o fim existente
            Long idExcluir             // ID do limite atual (para atualizações)
    );

    // Sobrecarga para o método de criação (onde não há ID para excluir)
    boolean existsByLimiteInicioLessThanEqualAndLimiteFimGreaterThanEqual(
            LocalTime limiteFim,
            LocalTime limiteInicio
    );
}