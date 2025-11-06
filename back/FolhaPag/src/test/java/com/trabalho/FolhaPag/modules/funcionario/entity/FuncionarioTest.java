package com.trabalho.FolhaPag.modules.funcionario.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FuncionarioEntity - testes básicos de construção e comportamento (Lombok)")
class FuncionarioTest {

    @Test
    @DisplayName("Builder deve construir objeto com valores informados")
    void builderConstrucaoCompleta() {
        LocalDate admissao = LocalDate.of(2024, 5, 20);

        Funcionario f = Funcionario.builder()
                .id(1L)
                .nome("João da Silva")
                .cpf("12345678901")
                .cargo("Desenvolvedor")
                .departamento("TI")
                .dataAdmissao(admissao)
                .salarioBruto(5000.0)
                .inss(500.0)
                .fgts(400.0)
                .irrf(350.0)
                .salarioLiquido(3750.0)
                .ativo(false)
                .horasPrevistas(160.0)
                .horasTrabalhadas(150.0)
                .valeTransporte(true)
                .numeroDependentes(2)
                .build();

        assertEquals(1L, f.getId());
        assertEquals("João da Silva", f.getNome());
        assertEquals("12345678901", f.getCpf());
        assertEquals("Desenvolvedor", f.getCargo());
        assertEquals("TI", f.getDepartamento());
        assertEquals(admissao, f.getDataAdmissao());
        assertEquals(5000.0, f.getSalarioBruto());
        assertEquals(500.0, f.getInss());
        assertEquals(400.0, f.getFgts());
        assertEquals(350.0, f.getIrrf());
        assertEquals(3750.0, f.getSalarioLiquido());
        assertFalse(f.getAtivo(), "Ativo foi definido como false explicitamente");
        assertEquals(160.0, f.getHorasPrevistas());
        assertEquals(150.0, f.getHorasTrabalhadas());
        assertTrue(f.getValeTransporte());
        assertEquals(2, f.getNumeroDependentes());
    }

    @Test
    @DisplayName("Construtor padrão deve aplicar default 'ativo' = true")
    void construtorPadraoValorDefaultAtivo() {
        Funcionario f = new Funcionario();
        assertTrue(f.getAtivo(), "Campo 'ativo' deve assumir default true no construtor padrão");
    }

    @Test
    @DisplayName("Equals e hashCode devem considerar todos os campos (Lombok @Data)")
    void equalsHashCode() {
        Funcionario a = Funcionario.builder()
                .id(10L)
                .nome("Carlos")
                .cpf("11122233344")
                .cargo("Analista")
                .departamento("Financeiro")
                .salarioBruto(4200.0)
                .ativo(true)
                .build();

        Funcionario b = Funcionario.builder()
                .id(10L) // mesmo id
                .nome("Carlos")
                .cpf("11122233344")
                .cargo("Analista")
                .departamento("Financeiro")
                .salarioBruto(4200.0)
                .ativo(true)
                .build();

        assertEquals(a, b, "Objetos com mesmos valores devem ser iguais");
        assertEquals(a.hashCode(), b.hashCode(), "hashCode deve ser igual para objetos iguais");

        // Diferencia por algum campo
        Funcionario c = Funcionario.builder()
                .id(11L) // id diferente
                .nome("Carlos")
                .cpf("11122233344")
                .cargo("Analista")
                .departamento("Financeiro")
                .salarioBruto(4200.0)
                .ativo(true)
                .build();

        assertNotEquals(a, c, "Objetos com id diferente não devem ser iguais");
    }

    @Test
    @DisplayName("Setters devem atualizar valores corretamente")
    void settersAtualizamValores() {
        Funcionario f = new Funcionario();
        f.setNome("Teste");
        f.setCpf("00011122233");
        f.setSalarioBruto(2500.0);
        f.setHorasPrevistas(160.0);
        f.setHorasTrabalhadas(120.0);
        f.setValeTransporte(false);
        f.setNumeroDependentes(0);

        assertEquals("Teste", f.getNome());
        assertEquals("00011122233", f.getCpf());
        assertEquals(2500.0, f.getSalarioBruto());
        assertEquals(160.0, f.getHorasPrevistas());
        assertEquals(120.0, f.getHorasTrabalhadas());
        assertFalse(f.getValeTransporte());
        assertEquals(0, f.getNumeroDependentes());
    }
}
