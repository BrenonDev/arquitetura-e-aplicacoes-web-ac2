package com.example.ac2.service;

import java.util.List;

import com.example.ac2.dto.DadosSetorDTO;
import com.example.ac2.dto.SetorDTO;

public interface SetorService {
    DadosSetorDTO adicionar(SetorDTO setor);
    DadosSetorDTO buscarSetorPorId(Integer idSetor);
    List<DadosSetorDTO> listar();
}
