package com.trabalho.FolhaPag.modules.folha.service;

import com.trabalho.FolhaPag.modules.folha.entity.FolhaPagamento;
import com.trabalho.FolhaPag.modules.folha.repository.FolhaPagamentoRepository;
import com.trabalho.FolhaPag.modules.funcionario.entity.Funcionario;
import com.trabalho.FolhaPag.modules.funcionario.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FolhaPagamentoService {

    private final FolhaPagamentoRepository folhaRepository;
    private final FuncionarioRepository funcionarioRepository;

    public List<FolhaPagamento> listarTodas() {
        return folhaRepository.findAll();
    }

    public Optional<FolhaPagamento> buscarPorId(Long id) {
        return folhaRepository.findById(id);
    }

    public FolhaPagamento criar(Long funcionarioId, LocalDate mesReferencia) {
        Funcionario funcionario = funcionarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado."));

        FolhaPagamento folha = FolhaPagamento.builder()
                .funcionario(funcionario)
                .mesReferencia(mesReferencia)
                .salarioBruto(funcionario.getSalarioBruto())
                .salarioLiquido(funcionario.getSalarioLiquido())
                .build();

        return folhaRepository.save(folha);
    }

    public void deletar(Long id) {
        folhaRepository.deleteById(id);
    }
}
