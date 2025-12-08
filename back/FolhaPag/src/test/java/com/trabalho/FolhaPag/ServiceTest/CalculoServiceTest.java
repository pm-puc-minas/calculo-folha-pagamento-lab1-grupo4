package com.trabalho.FolhaPag.ServiceTest;

import com.trabalho.FolhaPag.modules.folha.service.calculo.FolhaCalculoService;
import com.trabalho.FolhaPag.modules.folha.service.calculo.factory.ImpostoCalculadorFactory;
import com.trabalho.FolhaPag.modules.funcionario.entity.Funcionario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FolhaCalculoService - testes de impostos")
public class CalculoServiceTest {

    private final FolhaCalculoService calculoService = new FolhaCalculoService(
            new ImpostoCalculadorFactory()
    );

    @Test
    @DisplayName("Salário 1500.00 -> INSS, IRRF e FGTS esperados")
    void testCalcularTodos_Salario1500() {
        Funcionario funcionario = Funcionario.builder()
                .salarioBruto(1500.00)
                .numeroDependentes(0)
                .build();

        Map<String, Double> resultado = calculoService.calcularTodos(funcionario);

        assertNotNull(resultado);
        assertTrue(resultado.containsKey("INSS"));
        assertTrue(resultado.containsKey("IRRF"));
        assertTrue(resultado.containsKey("FGTS"));

        double esperadoInss = 115.47;
        double esperadoIrrf = 0.00; // base 1500 - 115.47 <= 1903.98
        double esperadoFgts = 120.00;

        assertEquals(esperadoInss, resultado.get("INSS"), 0.01);
        assertEquals(esperadoIrrf, resultado.get("IRRF"), 0.01);
        assertEquals(esperadoFgts, resultado.get("FGTS"), 0.01);
    }

    @Test
    @DisplayName("Salário 3500.00 -> INSS, IRRF e FGTS esperados com INSS deduzido da base do IRRF")
    void testCalcularTodos_Salario3500() {
        Funcionario funcionario = Funcionario.builder()
                .salarioBruto(3500.00)
                .numeroDependentes(0)
                .build();

        Map<String, Double> resultado = calculoService.calcularTodos(funcionario);

        assertNotNull(resultado);

        double esperadoInss = 323.33;
        double baseIrrf = 3500.00 - 323.33;
        double esperadoIrrf = baseIrrf * 0.15 - 354.80; // faixa 3
        double esperadoFgts = 280.00;

        assertEquals(esperadoInss, resultado.get("INSS"), 0.02);
        assertEquals(esperadoIrrf, resultado.get("IRRF"), 0.1);
        assertEquals(esperadoFgts, resultado.get("FGTS"), 0.01);
    }

    @Test
    @DisplayName("Salário 12000.00 -> INSS (teto efetivo), IRRF com INSS deduzido e FGTS")
    void testCalcularTodos_Salario12000() {
        Funcionario funcionario = Funcionario.builder()
                .salarioBruto(12000.00)
                .numeroDependentes(0)
                .build();

        Map<String, Double> resultado = calculoService.calcularTodos(funcionario);

        assertNotNull(resultado);

        double esperadoInss = 877.24;
        double baseIrrf = 12000.00 - 877.24;
        double esperadoIrrf = baseIrrf * 0.275 - 869.36; // faixa 5
        double esperadoFgts = 960.00;

        assertEquals(esperadoInss, resultado.get("INSS"), 0.01);
        assertEquals(esperadoIrrf, resultado.get("IRRF"), 0.5);
        assertEquals(esperadoFgts, resultado.get("FGTS"), 0.01);
    }
}
