package com.example.ac2.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ac2.config.exceptions.RegraNegocioException;
import com.example.ac2.domain.Funcionario;
import com.example.ac2.domain.Setor;
import com.example.ac2.dto.DadosSetorDTO;
import com.example.ac2.dto.FuncionarioResumoDTO;
import com.example.ac2.dto.SetorDTO;
import com.example.ac2.repository.SetorRepository;

@Service
public class SetorServiceImpl implements SetorService {
    private final SetorRepository setorRepository;

    public SetorServiceImpl(SetorRepository setorRepository) {
        this.setorRepository = setorRepository;
    }

    @Override
    @Transactional
    public DadosSetorDTO adicionar(SetorDTO dto) {
        return paraDados(setorRepository.save(new Setor(dto.nome().trim())));
    }

    @Override
    @Transactional(readOnly = true)
    public DadosSetorDTO buscarSetorPorId(Integer idSetor) {
        Setor setor = setorRepository.buscarComFuncionariosPorId(idSetor)
                .orElseThrow(() -> new RegraNegocioException("Setor não encontrado."));
        return paraDados(setor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DadosSetorDTO> listar() {
        return setorRepository.listarComFuncionarios().stream().map(this::paraDados).toList();
    }

    private DadosSetorDTO paraDados(Setor setor) {
        List<FuncionarioResumoDTO> funcionarios = setor.getFuncionarios().stream().map(this::paraResumo).toList();
        return new DadosSetorDTO(setor.getId(), setor.getNome(), funcionarios);
    }

    private FuncionarioResumoDTO paraResumo(Funcionario funcionario) {
        return new FuncionarioResumoDTO(funcionario.getId(), funcionario.getNome(), funcionario.getSetor().getId(),
                funcionario.getSetor().getNome());
    }
}
