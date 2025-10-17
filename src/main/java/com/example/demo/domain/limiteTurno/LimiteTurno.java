package com.example.demo.domain.limiteTurno;// package com.example.demo.domain.limitesturno;

import com.example.demo.domain.horario.Turno; // Reutilizamos o enum Turno
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(
    name = "limite_turno",
    uniqueConstraints = @UniqueConstraint(columnNames = {"turno"}) // Garante que só há um limite por turno
)
@Getter
@Setter
@NoArgsConstructor
public class LimiteTurno {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "turno", nullable = false)
    @Enumerated(EnumType.STRING)
    private Turno turno; // MANHA, TARDE, NOITE

    @Column(name = "limite_inicio", nullable = false)
    private LocalTime limiteInicio;

    @Column(name = "limite_fim", nullable = false)
    private LocalTime limiteFim;
    
    // Construtor para criação inicial (Admin ou Seed/População inicial)
    public LimiteTurno(Turno turno, LocalTime limiteInicio, LocalTime limiteFim) {
        this.turno = turno;
        this.limiteInicio = limiteInicio;
        this.limiteFim = limiteFim;
    }
    
    // Método de atualização (usado pelo Service)
    public void atualizar(LocalTime novoInicio, LocalTime novoFim) {
        if (novoInicio != null) {
            this.limiteInicio = novoInicio;
        }
        if (novoFim != null) {
            this.limiteFim = novoFim;
        }
        // Validação de negócio (início < fim) deve ser feita no Service/DTO
    }
}