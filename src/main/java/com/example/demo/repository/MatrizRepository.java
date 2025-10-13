package com.example.demo.repository;


import com.example.demo.domain.Matriz.Matriz;
import com.example.demo.domain.curso.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Repository
public interface MatrizRepository extends JpaRepository<Matriz, Long> {
    boolean existsByCursoAndAnoVigencia(Curso curso, Year anoVigencia);

    Optional<Matriz> findByCursoAndAnoVigencia(Curso curso, Year anoVigencia);
    // No MatrizRepository
    List<Matriz> findByCurso_Escola_Ativo(boolean ativo); // Ou Page<Matriz> se usar paginação
}