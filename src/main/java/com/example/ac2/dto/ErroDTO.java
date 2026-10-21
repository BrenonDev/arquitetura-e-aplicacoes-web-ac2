package com.example.ac2.dto;

import java.time.Instant;
import java.util.Map;

public record ErroDTO(Instant timestamp, int status, String erro, String mensagem, String caminho,
        Map<String, String> campos) {
}
