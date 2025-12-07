package com.trabalho.FolhaPag.modules.funcionario.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {
    private Long id;
    private String matricula;
    private String nome;
    private Boolean isAdmin;
    private String mensagem;
}
