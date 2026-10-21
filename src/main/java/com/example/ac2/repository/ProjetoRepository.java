package com.example.ac2.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ac2.domain.Projeto;

public interface ProjetoRepository extends JpaRepository<Projeto, Integer> {
    @Query("select distinct p from Projeto p left join fetch p.funcionarios where p.id = :id")
    Optional<Projeto> buscarComFuncionariosPorId(@Param("id") Integer id);

    @Query("select distinct p from Projeto p left join fetch p.funcionarios where p.dataInicio between :inicio and :fim order by p.dataInicio")
    List<Projeto> buscarPorIntervaloDeInicio(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    @Query("select distinct p from Projeto p left join fetch p.funcionarios order by p.dataInicio desc")
    List<Projeto> listarComFuncionarios();

}
