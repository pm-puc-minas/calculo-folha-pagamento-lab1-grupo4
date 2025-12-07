package com.trabalho.FolhaPag.modules.funcionario.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "funcionario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(name = "cargo", length = 60)
    private String cargo;

    @Column(name = "departamento", length = 60)
    private String departamento;

    @Column(name = "data_admissao")
    private LocalDate dataAdmissao;

    @Column(name = "salario_bruto", nullable = false)
    private Double salarioBruto;

    @Column(name = "inss")
    private Double inss;

    @Column(name = "fgts")
    private Double fgts;

    @Column(name = "irrf")
    private Double irrf;

    @Column(name = "salario_liquido")
    private Double salarioLiquido;

    @Builder.Default
    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "horas_previstas")
    private Double horasPrevistas;

    @Column(name = "horas_trabalhadas")
    private Double horasTrabalhadas;

    @Column(name = "vale_transporte")
    private Boolean valeTransporte;

    @Column(name = "numero_dependentes")
    private Integer numeroDependentes;

    @Column(nullable = false, unique = true, length = 10)
    private String matricula;

    @Column(length = 255, nullable = true)  
    private String senha;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isAdmin = false;

    @Column(name = "alimentacao")
    private Boolean alimentacao;
}
