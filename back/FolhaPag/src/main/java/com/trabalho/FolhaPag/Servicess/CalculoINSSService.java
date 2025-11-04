package com.trabalho.FolhaPag.Servicess;

import org.springframework.stereotype.Service;

@Service
public class CalculoINSSService implements ICalculo {

    @Override
    public double calcular(double salario) {
        if (salario <= 0) throw new IllegalArgumentException("Valor inválido.");

        double total;
        if (salario <= 1302.00)
            total = salario * 0.075;
        else if (salario >= 1302.01 && salario <= 2571.29)
            total = (1302.00 * 0.075) + ((salario - 1302.00) * 0.09);
        else if (salario >= 2571.30 && salario <= 3856.94)
            total = (1302.00 * 0.075) + ((2571.29 - 1302.00) * 0.09) + ((salario - 2571.29) * 0.12);
        else if (salario >= 3856.95 && salario <= 7507.49)
            total = (1302.00 * 0.075) + ((2571.29 - 1302.00) * 0.09) + ((3856.94 - 2571.29) * 0.12)
             + ((salario - 3856.94) * 0.14);
        else
            total = 877.24; // teto

        return Math.round(total * 100.0) / 100.0;
    }

    @Override
    public String getNome() {
        return "INSS";
    }
}
