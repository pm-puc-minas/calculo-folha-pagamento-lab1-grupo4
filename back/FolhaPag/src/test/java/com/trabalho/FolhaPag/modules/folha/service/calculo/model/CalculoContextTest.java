package com.trabalho.FolhaPag.modules.folha.service.calculo.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CalculoContext - testes de construção e valores nulos")
class CalculoContextTest {

    @Test
    @DisplayName("Builder armazena valores corretamente")
    void builderOk() {
        CalculoContext ctx = CalculoContext.builder()
                .salarioBruto(5000.0)
                .inss(500.0)
                .numeroDependentes(2)
                .build();

        assertEquals(5000.0, ctx.getSalarioBruto());
        assertEquals(500.0, ctx.getInss());
        assertEquals(2, ctx.getNumeroDependentes());
    }

    @Test
    @DisplayName("Campos opcionais podem ficar nulos")
    void camposNulos() {
        CalculoContext ctx = CalculoContext.builder()
                .salarioBruto(3000.0)
                .build();

        assertEquals(3000.0, ctx.getSalarioBruto());
        assertNull(ctx.getInss());
        assertNull(ctx.getNumeroDependentes());
    }
}
