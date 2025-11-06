package com.trabalho.FolhaPag.ServiceTest;

import com.trabalho.FolhaPag.modules.funcionario.Servicess.CFGTSService;
import com.trabalho.FolhaPag.modules.funcionario.Servicess.CINSSService;
import com.trabalho.FolhaPag.modules.funcionario.Servicess.CIRRFService;
import com.trabalho.FolhaPag.modules.funcionario.Servicess.CalculoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CalculoService - testes de impostos")
public class CalculoServiceTest {

    private final CalculoService calculoService = new CalculoService(
            Arrays.asList(
                    new CINSSService(),
                    new CIRRFService(),
                    new CFGTSService()
            )
    );

    @Test
    @DisplayName("Salário 1500.00 -> INSS, IRRF e FGTS esperados")
    void testCalcularTodos_Salario1500() {
        double salario = 1500.00;

        Map<String, Double> resultado = calculoService.calcularTodos(salario);

        assertNotNull(resultado, "Mapa de resultados não deve ser nulo");
        assertTrue(resultado.containsKey("INSS"), "Deve conter INSS");
        assertTrue(resultado.containsKey("IRRF"), "Deve conter IRRF");
        assertTrue(resultado.containsKey("FGTS"), "Deve conter FGTS");

        double esperadoInss = 113.82;
        double esperadoIrrf = 0.00;
        double esperadoFgts = 120.00;

        assertEquals(esperadoInss, resultado.get("INSS"), 0.01, "INSS incorreto para salário 1500.00");
        assertEquals(esperadoIrrf, resultado.get("IRRF"), 0.01, "IRRF incorreto para salário 1500.00");
        assertEquals(esperadoFgts, resultado.get("FGTS"), 0.01, "FGTS incorreto para salário 1500.00");
    }

    @Test
    @DisplayName("Salário 3500.00 -> INSS, IRRF e FGTS esperados")
    void testCalcularTodos_Salario3500() {
        double salario = 3500.00;

        Map<String, Double> resultado = calculoService.calcularTodos(salario);

        assertNotNull(resultado);

        double esperadoInss = 318.82;
        double esperadoIrrf = 122.38;
        double esperadoFgts = 280.00;

        assertEquals(esperadoInss, resultado.get("INSS"), 0.01, "INSS incorreto para salário 3500.00");
        assertEquals(esperadoIrrf, resultado.get("IRRF"), 0.01, "IRRF incorreto para salário 3500.00");
        assertEquals(esperadoFgts, resultado.get("FGTS"), 0.01, "FGTS incorreto para salário 3500.00");
    }

    @Test
    @DisplayName("Salário 12000.00 -> INSS (teto), IRRF e FGTS esperados")
    void testCalcularTodos_Salario12000() {
        double salario = 12000.00;

        Map<String, Double> resultado = calculoService.calcularTodos(salario);

        assertNotNull(resultado);

        double esperadoInss = 908.85;
        double esperadoIrrf = 2180.55;
        double esperadoFgts = 960.00;

        assertEquals(esperadoInss, resultado.get("INSS"), 0.01, "INSS incorreto para salário 12000.00");
        assertEquals(esperadoIrrf, resultado.get("IRRF"), 0.01, "IRRF incorreto para salário 12000.00");
        assertEquals(esperadoFgts, resultado.get("FGTS"), 0.01, "FGTS incorreto para salário 12000.00");
    }

    @Test
    @DisplayName("Salário 0.00 ou negativo -> calculoService deve lançar IllegalArgumentException")
    void testCalcularTodos_SalarioZeroOuNegativo_LancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> calculoService.calcularTodos(0.0));
        assertThrows(IllegalArgumentException.class, () -> calculoService.calcularTodos(-100.0));
    }
}
