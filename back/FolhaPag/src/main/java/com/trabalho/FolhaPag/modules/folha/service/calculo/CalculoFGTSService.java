package com.trabalho.FolhaPag.modules.folha.service.calculo;

import com.trabalho.FolhaPag.modules.folha.service.calculo.interfaces.ICalculo;
import com.trabalho.FolhaPag.modules.folha.service.calculo.model.CalculoContext;

import org.springframework.stereotype.Service;

@Service
public class CalculoFGTSService implements ICalculo {

    @Override
    public double calcular(CalculoContext context) {
        return context.getSalarioBruto() * 0.08;
    }

    @Override
    public String getTipo() {
        return "FGTS";
    }
}
