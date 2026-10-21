package com.example.ac2.dto;

import java.time.LocalDate;
import java.util.List;

public record DadosProjetoDTO(
        Integer id,
        String descricao,
        LocalDate dataInicio,
        LocalDate dataFim,
        List<FuncionarioResumoDTO> funcionarios) {
}
