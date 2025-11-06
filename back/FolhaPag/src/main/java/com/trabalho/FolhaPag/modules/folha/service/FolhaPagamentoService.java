package com.trabalho.FolhaPag.modules.folha.service;

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
    private final FolhaCalculoService folhaCalculoService; // <-- injeta o serviço de cálculo

    public List<FolhaPagamento> listarTodas() {
        return folhaRepository.findAll();
    }

    public Optional<FolhaPagamento> buscarPorId(Long id) {
        return folhaRepository.findById(id);
    }

    public FolhaPagamento criar(Long funcionarioId, LocalDate mesReferencia) {
        Funcionario funcionario = funcionarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado."));

        // Cria a folha base
        FolhaPagamento folha = FolhaPagamento.builder()
                .funcionario(funcionario)
                .mesReferencia(mesReferencia)
                .salarioBruto(funcionario.getSalarioBruto())
                .build();

        //  Calcula todos os tributos e benefícios (INSS, IRRF, FGTS, VT, VA etc.)
        Map<String, Double> resultados = folhaCalculoService.calcularTodos(funcionario);

        double totalDescontos = 0.0;
        double totalBeneficios = 0.0;

        //  Agrupa todos os calc 
        for (Map.Entry<String, Double> entry : resultados.entrySet()) {
            String tipo = entry.getKey().toUpperCase();
            double valor = entry.getValue();

            if (tipo.equals("INSS") || tipo.equals("IRRF") || tipo.equals("VT")) {
                totalDescontos += valor;
            } else if (tipo.equals("FGTS") || tipo.equals("VA")) {
                totalBeneficios += valor;
            }
        }

        double salarioLiquido = folha.getSalarioBruto() - totalDescontos + totalBeneficios;

        folha.setTotalDescontos(totalDescontos);
        folha.setTotalBeneficios(totalBeneficios);
        folha.setSalarioLiquido(salarioLiquido);

        return folhaRepository.save(folha);
    }

    public void deletar(Long id) {
        folhaRepository.deleteById(id);
    }
}
