package com.example.ac2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ac2.domain.Setor;

public interface SetorRepository extends JpaRepository<Setor, Integer> {
    @Query("select distinct s from Setor s left join fetch s.funcionarios where s.id = :id")
    Optional<Setor> buscarComFuncionariosPorId(@Param("id") Integer id);

    @Query("select distinct s from Setor s left join fetch s.funcionarios order by s.nome")
    List<Setor> listarComFuncionarios();
}
