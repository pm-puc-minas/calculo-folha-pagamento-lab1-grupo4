package com.trabalho.FolhaPag.modules.folha.service.calculo.factory;

import com.trabalho.FolhaPag.modules.folha.service.calculo.interfaces.ICalculo;
import com.trabalho.FolhaPag.modules.folha.service.calculo.CalculoINSSService;
import com.trabalho.FolhaPag.modules.folha.service.calculo.CalculoIRRFService;
import com.trabalho.FolhaPag.modules.folha.service.calculo.CalculoFGTSService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ImpostoCalculadorFactory {

    private final Map<TipoImposto, ICalculo> calculadores;

    public ImpostoCalculadorFactory() {
        this.calculadores = new HashMap<>();
        registrarCalculadores();
    }

    private void registrarCalculadores() {
        calculadores.put(TipoImposto.INSS, new CalculoINSSService());
        calculadores.put(TipoImposto.IRRF, new CalculoIRRFService());
        calculadores.put(TipoImposto.FGTS, new CalculoFGTSService());
    }

    public ICalculo obterCalculador(TipoImposto tipo) {
        ICalculo calculador = calculadores.get(tipo);
        if (calculador == null) {
            throw new IllegalArgumentException(
                "Calculador não encontrado para o tipo: " + tipo.getCodigo()
            );
        }
        return calculador;
    }

    public ICalculo obterCalculadorPorCodigo(String codigoImposto) {
        TipoImposto tipo = TipoImposto.fromCodigo(codigoImposto);
        return obterCalculador(tipo);
    }

    public boolean isCalculadorDisponivel(TipoImposto tipo) {
        return calculadores.containsKey(tipo);
    }

    public TipoImposto[] getTiposDisponiveis() {
        return calculadores.keySet().toArray(new TipoImposto[0]);
    }
}
