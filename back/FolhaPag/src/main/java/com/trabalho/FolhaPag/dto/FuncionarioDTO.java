package com.trabalho.FolhaPag.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuncionarioDTO {
    private String nome;
    private String cpf;
    private String cargo;
    private String departamento;
    private Double salarioBruto;
    private LocalDate dataAdmissao;
    private Double horasPrevistas;
    private Double horasTrabalhadas;
    private Boolean valeTransporte;
    private Integer numeroDependentes;
}
