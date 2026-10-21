package com.example.ac2.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProjetoDTO(
        @NotBlank(message = "A descrição do projeto é obrigatória.") String descricao,
        @NotNull(message = "A data de início é obrigatória.") LocalDate dataInicio,
        @NotNull(message = "A data de fim é obrigatória.") LocalDate dataFim) {
}
