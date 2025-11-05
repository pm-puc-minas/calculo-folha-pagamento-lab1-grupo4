
package com.trabalho.FolhaPag.modules.funcionario.Servicess;

import org.springframework.stereotype.Service;

@Service
public class CalculoFGTSService implements ICalculo {

    @Override
    public double calcular(double salario) {
        return Math.round(salario * 0.08 * 100.0) / 100.0;
    }

    @Override
    public String getNome() {
        return "FGTS";
    }
}
