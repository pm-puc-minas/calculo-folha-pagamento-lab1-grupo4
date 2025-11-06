package com.trabalho.FolhaPag.modules.folha.service.calculo;

import com.trabalho.FolhaPag.modules.funcionario.entity.Funcionario;
import com.trabalho.FolhaPag.modules.folha.service.calculo.interfaces.ICalculo;
import com.trabalho.FolhaPag.modules.folha.service.calculo.model.CalculoContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FolhaCalculoService - integração de cálculos sequenciais")
class FolhaCalculoServiceTest {

    // Implementações mínimas fake para isolar lógica de ordenação/contexto
    static class INSSFake implements ICalculo {
        @Override
        public double calcular(CalculoContext context) {
            double sal = context.getSalarioBruto();
            if (sal <= 1302.00) return round(sal * 0.075);
            return 100.00; // simplificado para teste
        }
        @Override
        public String getTipo() { return "INSS"; }
    }

    static class IRRFFake implements ICalculo {
        @Override
        public double calcular(CalculoContext context) {
            double base = context.getSalarioBruto() - (context.getInss() != null ? context.getInss() : 0);
            if (base <= 1903.98) return 0;
            return round(base * 0.075 - 142.80);
        }
        @Override
        public String getTipo() { return "IRRF"; }
    }

    static class FGTSFake implements ICalculo {
        @Override
        public double calcular(CalculoContext context) { return round(context.getSalarioBruto() * 0.08); }
        @Override
        public String getTipo() { return "FGTS"; }
    }

    private static double round(double v) { return Math.round(v * 100.0) / 100.0; }

    @Test
    @DisplayName("Ordem garante que IRRF recebe INSS previamente calculado")
    void ordemCalculoInssAntesIrrf() {
        FolhaCalculoService service = new FolhaCalculoService(List.of(
                new INSSFake(), new IRRFFake(), new FGTSFake()
        ));

        Funcionario f = Funcionario.builder()
                .salarioBruto(1500.00)
                .numeroDependentes(0)
                .build();

        Map<String, Double> r = service.calcularTodos(f);

        assertEquals(3, r.size());
        assertTrue(r.containsKey("INSS"));
        assertTrue(r.containsKey("IRRF"));
        assertTrue(r.containsKey("FGTS"));

        // INSS faixa baixa 1500 -> simplificado: não está na primeira regra fictícia, usamos 100.00? (1500>1302) então 100.00
        assertEquals(100.00, r.get("INSS"));

        // IRRF base = 1500 - 100 = 1400 <= 1903.98 => 0
        assertEquals(0.0, r.get("IRRF"));

        // FGTS 8% arredondado
        assertEquals(120.00, r.get("FGTS"));
    }

    @Test
    @DisplayName("Contexto sem INSS prévio calcula e injeta para IRRF")
    void calculaInssQuandoNaoExisteNoFuncionario() {
        FolhaCalculoService service = new FolhaCalculoService(List.of(
                new INSSFake(), new IRRFFake()
        ));

        Funcionario f = Funcionario.builder()
                .salarioBruto(1000.00)
                .build();

        Map<String, Double> r = service.calcularTodos(f);
        assertEquals(2, r.size());
        assertEquals(75.00, r.get("INSS")); // 1000 * 7.5%
        // base = 1000 - 75 <= 1903.98 -> 0
        assertEquals(0.0, r.get("IRRF"));
    }
}
