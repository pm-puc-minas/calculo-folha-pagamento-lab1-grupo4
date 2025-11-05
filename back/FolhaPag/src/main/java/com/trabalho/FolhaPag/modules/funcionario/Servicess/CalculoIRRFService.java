package com.trabalho.FolhaPag.modules.funcionario.Servicess;

import org.springframework.stereotype.Service;

@Service
public class CalculoIRRFService implements ICalculo {

    @Override
    public double calcular(double baseDeCalculo) {
        // CORREÇÃO APLICADA AQUI: chamando o método correto 'paraBaseDeCalculo'
        FaixaIRRF faixa = FaixaIRRF.paraBaseDeCalculo(baseDeCalculo);

        double aliquota = faixa.getAliquota();
        double deducao = faixa.getParcelaADeduzir();

        double imposto = (baseDeCalculo * aliquota) - deducao;

        return Math.max(0, Math.round(imposto * 100.0) / 100.0);
    }

    @Override
    public String getNome() {
        return "IRRF";
    }
}
