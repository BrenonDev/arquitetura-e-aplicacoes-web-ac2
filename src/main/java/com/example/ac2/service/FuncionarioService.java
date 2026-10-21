package com.example.ac2.service;

import java.util.List;

import com.example.ac2.dto.DadosProjetoDTO;
import com.example.ac2.dto.FuncionarioDTO;
import com.example.ac2.dto.FuncionarioResumoDTO;

public interface FuncionarioService {
    FuncionarioResumoDTO adicionar(FuncionarioDTO funcionario);
    List<DadosProjetoDTO> buscarProjetos(Integer idFuncionario);
}
