package com.example.ac2.dto;

import jakarta.validation.constraints.NotBlank;

public record SetorDTO(
        @NotBlank(message = "O nome do setor é obrigatório.") String nome) {
}
