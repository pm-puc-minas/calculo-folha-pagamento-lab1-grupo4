package com.trabalho.FolhaPag.modules.folha.service;

import com.trabalho.FolhaPag.Exceptions.ValueNotValidException;
import com.trabalho.FolhaPag.modules.folha.entity.FolhaPagamento;
import com.trabalho.FolhaPag.modules.folha.repository.FolhaPagamentoRepository;
import com.trabalho.FolhaPag.modules.folha.service.calculo.FolhaCalculoService;
import com.trabalho.FolhaPag.modules.funcionario.entity.Funcionario;
import com.trabalho.FolhaPag.modules.funcionario.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FolhaPagamentoService {

    private final FolhaPagamentoRepository folhaRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final FolhaCalculoService folhaCalculoService;

    public List<FolhaPagamento> listarTodas() {
        try {
            List<FolhaPagamento> folhas = folhaRepository.findAllWithFuncionario();
            recalcularFolhasSeNecessario(folhas);
            return folhas;
        } catch (Exception ex) {
            List<FolhaPagamento> folhas = folhaRepository.findAll();
            recalcularFolhasSeNecessario(folhas);
            return folhas;
        }
    }

    public List<FolhaPagamento> listarPorMatricula(String matricula) {
        if (matricula == null) return List.of();
        var funcionario = funcionarioRepository.findByMatricula(matricula);
        if (funcionario == null) return List.of();
        List<FolhaPagamento> folhas = folhaRepository.findByFuncionario(funcionario);
        recalcularFolhasSeNecessario(folhas);
        return folhas;
    }

    private void recalcularFolhasSeNecessario(List<FolhaPagamento> folhas) {
        for (FolhaPagamento folha : folhas) {
            if (folha.getInss() == null || folha.getInss() == 0.0) {
                recalcularFolha(folha);
            }
        }
    }

    private void recalcularFolha(FolhaPagamento folha) {
        Funcionario funcionario = folha.getFuncionario();
        if (funcionario == null) return;

        Map<String, Double> resultados = folhaCalculoService.calcularTodos(funcionario);

        double inss = resultados.getOrDefault("INSS", 0.0);
        double irrf = resultados.getOrDefault("IRRF", 0.0);
        double fgts = resultados.getOrDefault("FGTS", 0.0);
        double valeTransporte = 0.0;

        if (Boolean.TRUE.equals(funcionario.getValeTransporte())) {
            valeTransporte = funcionario.getSalarioBruto() * 0.06;
        }

        double totalDescontos = inss + irrf + valeTransporte;
        double totalBeneficios = fgts;
        double salarioLiquido = folha.getSalarioBruto() - totalDescontos + totalBeneficios;

        folha.setInss(inss);
        folha.setIrrf(irrf);
        folha.setFgts(fgts);
        folha.setValeTransporte(valeTransporte);
        folha.setTotalDescontos(totalDescontos);
        folha.setTotalBeneficios(totalBeneficios);
        folha.setSalarioLiquido(salarioLiquido);

        folhaRepository.save(folha);
    }

    public Optional<FolhaPagamento> buscarPorId(Long id) {
        return folhaRepository.findById(id);
    }

    public FolhaPagamento criar(Long funcionarioId, LocalDate mesReferencia) {
        Funcionario funcionario = funcionarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new ValueNotValidException("Funcionário não encontrado."));

        if (funcionario.getSalarioBruto() <= 0) {
            throw new ValueNotValidException("Salário bruto inválido");
        }

    // Cria a folha base
        FolhaPagamento folha = FolhaPagamento.builder()
                .funcionario(funcionario)
                .mesReferencia(mesReferencia)
                .salarioBruto(funcionario.getSalarioBruto())
                .build();


    // Calcula tributos e benefícios
        Map<String, Double> resultados = folhaCalculoService.calcularTodos(funcionario);

        double inss = resultados.getOrDefault("INSS", 0.0);
        double irrf = resultados.getOrDefault("IRRF", 0.0);
        double fgts = resultados.getOrDefault("FGTS", 0.0);
        double valeTransporte = 0.0;

        if (Boolean.TRUE.equals(funcionario.getValeTransporte())) {
            valeTransporte = funcionario.getSalarioBruto() * 0.06;
        }

        double totalDescontos = inss + irrf + valeTransporte;
        double totalBeneficios = fgts;
        double salarioLiquido = folha.getSalarioBruto() - totalDescontos + totalBeneficios;

        folha.setInss(inss);
        folha.setIrrf(irrf);
        folha.setFgts(fgts);
        folha.setValeTransporte(valeTransporte);
        folha.setTotalDescontos(totalDescontos);
        folha.setTotalBeneficios(totalBeneficios);
        folha.setSalarioLiquido(salarioLiquido);

        return folhaRepository.save(folha);
    }

    public void deletar(Long id) {
        folhaRepository.deleteById(id);
    }
}
