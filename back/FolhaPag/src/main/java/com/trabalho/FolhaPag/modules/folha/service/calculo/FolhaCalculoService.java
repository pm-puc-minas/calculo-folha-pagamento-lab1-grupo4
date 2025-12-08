package com.trabalho.FolhaPag.modules.folha.service.calculo;

import com.trabalho.FolhaPag.modules.folha.service.calculo.factory.ImpostoCalculadorFactory;
import com.trabalho.FolhaPag.modules.folha.service.calculo.factory.TipoImposto;
import com.trabalho.FolhaPag.modules.folha.service.calculo.model.CalculoContext;
import com.trabalho.FolhaPag.modules.funcionario.entity.Funcionario;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FolhaCalculoService {

    private final ImpostoCalculadorFactory impostoFactory;

    public Map<String, Double> calcularTodos(Funcionario funcionario) {
        Map<String, Double> resultados = new HashMap<>();

        CalculoContext baseContext = CalculoContext.builder()
                .salarioBruto(funcionario.getSalarioBruto())
                .numeroDependentes(funcionario.getNumeroDependentes())
                .build();

        Double inssCalculado = impostoFactory.obterCalculador(TipoImposto.INSS)
                .calcular(baseContext);
        resultados.put(TipoImposto.INSS.getCodigo(), inssCalculado);

        CalculoContext contextComInss = CalculoContext.builder()
                .salarioBruto(baseContext.getSalarioBruto())
                .numeroDependentes(baseContext.getNumeroDependentes())
                .inss(inssCalculado)
                .build();

        double irrfCalculado = impostoFactory.obterCalculador(TipoImposto.IRRF)
                .calcular(contextComInss);
        resultados.put(TipoImposto.IRRF.getCodigo(), irrfCalculado);

        double fgtsCalculado = impostoFactory.obterCalculador(TipoImposto.FGTS)
                .calcular(baseContext);
        resultados.put(TipoImposto.FGTS.getCodigo(), fgtsCalculado);

        return resultados;
    }

    public double calcularEspecifico(Funcionario funcionario, TipoImposto tipoImposto) {
        CalculoContext context = CalculoContext.builder()
                .salarioBruto(funcionario.getSalarioBruto())
                .numeroDependentes(funcionario.getNumeroDependentes())
                .inss(funcionario.getInss())
                .build();

        return impostoFactory.obterCalculador(tipoImposto).calcular(context);
    }
}
