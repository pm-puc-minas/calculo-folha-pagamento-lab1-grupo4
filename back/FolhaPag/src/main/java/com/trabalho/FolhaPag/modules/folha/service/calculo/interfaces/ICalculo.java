package com.trabalho.FolhaPag.modules.folha.service.calculo.interfaces;

import com.trabalho.FolhaPag.modules.folha.service.calculo.model.CalculoContext;

public interface ICalculo {
    double calcular(CalculoContext context);
    String getTipo();
}