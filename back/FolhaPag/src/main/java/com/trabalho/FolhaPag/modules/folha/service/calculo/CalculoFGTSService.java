package com.trabalho.FolhaPag.modules.folha.service.calculo;

import com.trabalho.FolhaPag.modules.folha.service.calculo.interfaces.ICalculo;
import com.trabalho.FolhaPag.modules.funcionario.entity.Funcionario;

import org.springframework.stereotype.Service;

@Service
public class CalculoFGTSService implements ICalculo {

    @Override
    public double calcular(Funcionario funcionario) {
        return funcionario.getSalarioBruto() * 0.08;
    }

    @Override
    public String getTipo() {
        return "FGTS";
    }
}
