package com.trabalho.FolhaPag.modules.folha.service.calculo;

import com.trabalho.FolhaPag.modules.folha.service.calculo.interfaces.ICalculo;
import com.trabalho.FolhaPag.modules.folha.service.calculo.model.CalculoContext;
import org.springframework.stereotype.Service;


@Service
public class CalculoINSSService implements ICalculo {

    @Override
    public double calcular(CalculoContext context) {
        double salario = context.getSalarioBruto();
        double total = 0.0;

        double faixa1 = 1302.00;
        double faixa2 = 2571.29;
        double faixa3 = 3856.94;
        double faixa4 = 7507.49;

        if (salario <= faixa1) {
            total = salario * 0.075;
        } else if (salario <= faixa2) {
            total = (faixa1 * 0.075) + ((salario - faixa1) * 0.09);
        } else if (salario <= faixa3) {
            total = (faixa1 * 0.075) + ((faixa2 - faixa1) * 0.09) + ((salario - faixa2) * 0.12);
        } else if (salario <= faixa4) {
            total = (faixa1 * 0.075) + ((faixa2 - faixa1) * 0.09) + ((faixa3 - faixa2) * 0.12) + ((salario - faixa3) * 0.14);
        } else {
           
            total = (faixa1 * 0.075) + ((faixa2 - faixa1) * 0.09) + ((faixa3 - faixa2) * 0.12) + ((faixa4 - faixa3) * 0.14);
        }

        return total;
    }

    @Override
    public String getTipo() {
        return "INSS";
    }
}
