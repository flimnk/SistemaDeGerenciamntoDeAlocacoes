package com.example.demo.repository;

import com.example.demo.domain.disciplina.Disciplina;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
    boolean existsByNome( String  nome) ;

    boolean existsByDescricao(String descricao);

    List<Disciplina> findByAtivoTrue();
}