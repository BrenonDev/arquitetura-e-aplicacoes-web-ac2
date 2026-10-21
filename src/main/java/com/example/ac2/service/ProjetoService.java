package com.example.ac2.service;

import java.time.LocalDate;
import java.util.List;

import com.example.ac2.dto.DadosProjetoDTO;
import com.example.ac2.dto.ProjetoDTO;

public interface ProjetoService {
    DadosProjetoDTO adicionar(ProjetoDTO projeto);
    List<DadosProjetoDTO> listar(LocalDate inicio, LocalDate fim);
    DadosProjetoDTO buscarProjetoPorId(Integer id);
    DadosProjetoDTO vincularFuncionario(Integer idProjeto, Integer idFuncionario);
}
