package com.trabalho.FolhaPag.modules.folha.entity;

import com.trabalho.FolhaPag.modules.funcionario.entity.Funcionario;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "folha_pagamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FolhaPagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento com o funcionário
    @ManyToOne
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Funcionario funcionario;

    @Column(name = "mes_referencia", nullable = false)
    private LocalDate mesReferencia;

    @Column(name = "salario_bruto", nullable = false)
    private Double salarioBruto;

    @Column(name = "salario_liquido")
    private Double salarioLiquido;

    @Column(name = "total_descontos")
    private Double totalDescontos = 0.0;

    @Column(name = "total_beneficios")
    private Double totalBeneficios = 0.0;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm = LocalDateTime.now();
}
