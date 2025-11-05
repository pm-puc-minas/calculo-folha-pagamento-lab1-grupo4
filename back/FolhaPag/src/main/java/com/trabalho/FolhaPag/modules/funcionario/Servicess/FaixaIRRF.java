package com.trabalho.FolhaPag.modules.funcionario.Servicess;

public enum FaixaIRRF {
    FAIXA_1(1903.98, 0.0, 0.0),
    FAIXA_2(2826.65, 0.075, 142.80),
    FAIXA_3(3751.05, 0.15, 354.80),
    FAIXA_4(4664.68, 0.225, 636.13),
    FAIXA_5(Double.MAX_VALUE, 0.275, 869.36);

    private final double limiteSuperior;
    private final double aliquota;
    private final double parcelaADeduzir;

    FaixaIRRF(double limiteSuperior, double aliquota, double parcelaADeduzir) {
        this.limiteSuperior = limiteSuperior;
        this.aliquota = aliquota;
        this.parcelaADeduzir = parcelaADeduzir;
    }

    public double getAliquota() {
        return aliquota;
    }

    public double getParcelaADeduzir() {
        return parcelaADeduzir;
    }

    public static FaixaIRRF paraBaseDeCalculo(double baseDeCalculo) {
        for (FaixaIRRF faixa : values()) {
            if (baseDeCalculo <= faixa.limiteSuperior) {
                return faixa;
            }
        }
        return FAIXA_5;
    }
}
