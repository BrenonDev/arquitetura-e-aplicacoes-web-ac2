package com.example.ac2.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.ac2.dto.DadosProjetoDTO;
import com.example.ac2.dto.FuncionarioDTO;
import com.example.ac2.dto.FuncionarioResumoDTO;
import com.example.ac2.service.FuncionarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {
    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FuncionarioResumoDTO adicionar(@Valid @RequestBody FuncionarioDTO funcionario) {
        return funcionarioService.adicionar(funcionario);
    }

    @GetMapping("/{idFuncionario}/projetos")
    public List<DadosProjetoDTO> buscarProjetos(@PathVariable Integer idFuncionario) {
        return funcionarioService.buscarProjetos(idFuncionario);
    }
}
