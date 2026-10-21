package com.example.ac2.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ac2.config.exceptions.RegraNegocioException;
import com.example.ac2.domain.Funcionario;
import com.example.ac2.domain.Projeto;
import com.example.ac2.dto.DadosProjetoDTO;
import com.example.ac2.dto.FuncionarioResumoDTO;
import com.example.ac2.dto.ProjetoDTO;
import com.example.ac2.repository.FuncionarioRepository;
import com.example.ac2.repository.ProjetoRepository;

@Service
public class ProjetoServiceImpl implements ProjetoService {
    private final ProjetoRepository projetoRepository;
    private final FuncionarioRepository funcionarioRepository;

    public ProjetoServiceImpl(ProjetoRepository projetoRepository, FuncionarioRepository funcionarioRepository) {
        this.projetoRepository = projetoRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    @Override
    @Transactional
    public DadosProjetoDTO adicionar(ProjetoDTO dto) {
        validarDatas(dto.dataInicio(), dto.dataFim());
        return paraDados(projetoRepository.save(new Projeto(dto.descricao().trim(), dto.dataInicio(), dto.dataFim())));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DadosProjetoDTO> listar(LocalDate inicio, LocalDate fim) {
        if (inicio == null && fim == null) {
            return projetoRepository.listarComFuncionarios().stream().map(this::paraDados).toList();
        }
        if (inicio == null || fim == null) {
            throw new RegraNegocioException("Informe as duas datas para filtrar os projetos.");
        }
        validarDatas(inicio, fim);
        return projetoRepository.buscarPorIntervaloDeInicio(inicio, fim).stream().map(this::paraDados).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DadosProjetoDTO buscarProjetoPorId(Integer id) {
        Projeto projeto = projetoRepository.buscarComFuncionariosPorId(id)
                .orElseThrow(() -> new RegraNegocioException("Projeto não encontrado."));
        return paraDados(projeto);
    }

    @Override
    @Transactional
    public DadosProjetoDTO vincularFuncionario(Integer idProjeto, Integer idFuncionario) {
        Projeto projeto = projetoRepository.buscarComFuncionariosPorId(idProjeto)
                .orElseThrow(() -> new RegraNegocioException("Projeto não encontrado."));
        Funcionario funcionario = funcionarioRepository.findById(idFuncionario)
                .orElseThrow(() -> new RegraNegocioException("Funcionário não encontrado."));

        if (projeto.getFuncionarios().stream().anyMatch(atual -> atual.getId().equals(idFuncionario))) {
            throw new RegraNegocioException("Este funcionário já está vinculado ao projeto.");
        }
        projeto.getFuncionarios().add(funcionario);
        funcionario.getProjetos().add(projeto);
        projetoRepository.save(projeto);
        return paraDados(projeto);
    }

    private void validarDatas(LocalDate inicio, LocalDate fim) {
        if (fim.isBefore(inicio)) {
            throw new RegraNegocioException("A data de fim não pode ser anterior à data de início.");
        }
    }

    private DadosProjetoDTO paraDados(Projeto projeto) {
        List<FuncionarioResumoDTO> funcionarios = projeto.getFuncionarios().stream()
                .map(funcionario -> new FuncionarioResumoDTO(
                        funcionario.getId(), funcionario.getNome(), funcionario.getSetor().getId(),
                        funcionario.getSetor().getNome()))
                .toList();
        return new DadosProjetoDTO(projeto.getId(), projeto.getDescricao(), projeto.getDataInicio(),
                projeto.getDataFim(), funcionarios);
    }
}
