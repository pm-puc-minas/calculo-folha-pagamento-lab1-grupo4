package com.trabalho.FolhaPag.Service;

import org.springframework.stereotype.Service;

@Service
public class CalculoIRRFService implements ICalculo {

    @Override
    public double calcular(double salario) {
        double aliquota;
        double deducao;

        if (salario <= 2112.00) {
            aliquota = 0;
            deducao = 0;
        } else if (salario <= 2826.65) {
            aliquota = 0.075;
            //a dedução é salario * aliquota
            //colocar a aliquota como valorem em um enum
            deducao = 158.40;
        } else if (salario <= 3751.05) {
            aliquota = 0.15;
            deducao = 370.40;
        } else if (salario <= 4664.68) {
            aliquota = 0.225;
            deducao = 651.73;
        } else {
            aliquota = 0.275;
            deducao = 884.96;
        }

        double imposto = (salario * aliquota) - deducao;
        return Math.max(0, Math.round(imposto * 100.0) / 100.0);
    }

    @Override
    public String getNome() {
        return "IRRF";
    }
}
