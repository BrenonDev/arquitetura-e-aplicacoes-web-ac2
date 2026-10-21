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

import com.example.ac2.dto.DadosSetorDTO;
import com.example.ac2.dto.SetorDTO;
import com.example.ac2.service.SetorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/setores")
public class SetorController {
    private final SetorService setorService;

    public SetorController(SetorService setorService) {
        this.setorService = setorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DadosSetorDTO adicionar(@Valid @RequestBody SetorDTO setor) {
        return setorService.adicionar(setor);
    }

    @GetMapping
    public List<DadosSetorDTO> listar() {
        return setorService.listar();
    }

    @GetMapping("/{idSetor}")
    public DadosSetorDTO buscarSetorPorId(@PathVariable Integer idSetor) {
        return setorService.buscarSetorPorId(idSetor);
    }
}
