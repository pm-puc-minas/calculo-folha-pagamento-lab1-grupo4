package com.trabalho.FolhaPag.modules.folha.service.calculo;

import com.trabalho.FolhaPag.modules.folha.service.calculo.interfaces.ICalculo;
import com.trabalho.FolhaPag.modules.folha.service.calculo.model.CalculoContext;
import org.springframework.stereotype.Service;


@Service
public class CalculoIRRFService implements ICalculo {

    @Override
    public double calcular(CalculoContext context) {
        double base = context.getSalarioBruto() - (context.getInss() != null ? context.getInss() : 0);
        double deducaoDependentes = (context.getNumeroDependentes() != null ? context.getNumeroDependentes() : 0) * 189.59;
        base -= deducaoDependentes;

        if (base <= 1903.98) return 0;
        else if (base <= 2826.65) return base * 0.075 - 142.80;
        else if (base <= 3751.05) return base * 0.15 - 354.80;
        else if (base <= 4664.68) return base * 0.225 - 636.13;
        else return base * 0.275 - 869.36;
    }

    @Override
    public String getTipo() {
        return "IRRF";
    }
}
