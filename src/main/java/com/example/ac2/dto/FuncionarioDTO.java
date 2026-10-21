package com.example.ac2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FuncionarioDTO(
        @NotBlank(message = "O nome do funcionário é obrigatório.") String nome,
        @NotNull(message = "O setor do funcionário é obrigatório.") Integer setorId) {
}
