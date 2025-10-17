package com.example.demo.repository;

import com.example.demo.domain.formacao.Formacao;
import com.example.demo.domain.professor.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FormacaoRepository extends JpaRepository<Formacao, Long> {

    Optional<Formacao> findByProfessor(Professor  professor);
}