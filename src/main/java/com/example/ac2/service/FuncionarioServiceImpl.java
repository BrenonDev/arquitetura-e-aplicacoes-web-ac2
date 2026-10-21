package com.example.ac2.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ac2.config.exceptions.RegraNegocioException;
import com.example.ac2.domain.Funcionario;
import com.example.ac2.domain.Projeto;
import com.example.ac2.domain.Setor;
import com.example.ac2.dto.DadosProjetoDTO;
import com.example.ac2.dto.FuncionarioDTO;
import com.example.ac2.dto.FuncionarioResumoDTO;
import com.example.ac2.repository.FuncionarioRepository;
import com.example.ac2.repository.SetorRepository;

@Service
public class FuncionarioServiceImpl implements FuncionarioService {
    private final FuncionarioRepository funcionarioRepository;
    private final SetorRepository setorRepository;

    public FuncionarioServiceImpl(FuncionarioRepository funcionarioRepository, SetorRepository setorRepository) {
        this.funcionarioRepository = funcionarioRepository;
        this.setorRepository = setorRepository;
    }

    @Override
    @Transactional
    public FuncionarioResumoDTO adicionar(FuncionarioDTO dto) {
        Setor setor = setorRepository.findById(dto.setorId())
                .orElseThrow(() -> new RegraNegocioException("Setor não encontrado."));
        Funcionario funcionario = funcionarioRepository.save(new Funcionario(dto.nome().trim(), setor));
        return paraResumo(funcionario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DadosProjetoDTO> buscarProjetos(Integer idFuncionario) {
        if (!funcionarioRepository.existsById(idFuncionario)) {
            throw new RegraNegocioException("Funcionário não encontrado.");
        }
        return funcionarioRepository.buscarProjetosPorFuncionario(idFuncionario).stream().map(this::paraDados).toList();
    }

    private DadosProjetoDTO paraDados(Projeto projeto) {
        return new DadosProjetoDTO(projeto.getId(), projeto.getDescricao(), projeto.getDataInicio(),
                projeto.getDataFim(), projeto.getFuncionarios().stream().map(this::paraResumo).toList());
    }

    private FuncionarioResumoDTO paraResumo(Funcionario funcionario) {
        return new FuncionarioResumoDTO(funcionario.getId(), funcionario.getNome(), funcionario.getSetor().getId(),
                funcionario.getSetor().getNome());
    }
}
