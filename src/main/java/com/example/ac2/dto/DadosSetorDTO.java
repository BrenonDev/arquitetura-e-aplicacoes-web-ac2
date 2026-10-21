package com.example.ac2.dto;

import java.util.List;

public record DadosSetorDTO(Integer id, String nome, List<FuncionarioResumoDTO> funcionarios) {
}
