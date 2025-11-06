package com.trabalho.FolhaPag.modules.folha.service.calculo.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CalculoContext {
    Double salarioBruto;
    Double inss; // pode ser nulo quando ainda não calculado
    Integer numeroDependentes;
}
