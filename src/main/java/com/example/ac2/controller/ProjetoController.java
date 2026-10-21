package com.example.ac2.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.ac2.dto.DadosProjetoDTO;
import com.example.ac2.dto.ProjetoDTO;
import com.example.ac2.service.ProjetoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/projetos")
public class ProjetoController {
    private final ProjetoService projetoService;

    public ProjetoController(ProjetoService projetoService) {
        this.projetoService = projetoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DadosProjetoDTO adicionar(@Valid @RequestBody ProjetoDTO projeto) {
        return projetoService.adicionar(projeto);
    }

    @GetMapping
    public List<DadosProjetoDTO> listar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return projetoService.listar(inicio, fim);
    }

    @GetMapping("/{id}")
    public DadosProjetoDTO buscarProjetoPorId(@PathVariable Integer id) {
        return projetoService.buscarProjetoPorId(id);
    }

    @PutMapping("/{idProjeto}/funcionarios/{idFuncionario}")
    public DadosProjetoDTO vincularFuncionario(@PathVariable Integer idProjeto, @PathVariable Integer idFuncionario) {
        return projetoService.vincularFuncionario(idProjeto, idFuncionario);
    }
}
