package com.trabalho.FolhaPag.modules.folha.service.calculo.interfaces;

import com.trabalho.FolhaPag.modules.funcionario.entity.Funcionario;

public interface ICalculo {
    double calcular(Funcionario funcionario);
    String getTipo();
}