package com.example.ac2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ac2.domain.Funcionario;
import com.example.ac2.domain.Projeto;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {
	@Query("select distinct p from Funcionario f join f.projetos p left join fetch p.funcionarios where f.id = :idFuncionario")
	List<Projeto> buscarProjetosPorFuncionario(@Param("idFuncionario") Integer idFuncionario);
}
