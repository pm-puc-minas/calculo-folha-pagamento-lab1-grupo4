package com.trabalho.FolhaPag.modules.folha.service.calculo;

import com.trabalho.FolhaPag.modules.folha.service.calculo.interfaces.ICalculo;
import com.trabalho.FolhaPag.modules.folha.service.calculo.model.CalculoContext;
import com.trabalho.FolhaPag.modules.funcionario.entity.Funcionario;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FolhaCalculoService {

    private final List<ICalculo> calculos; 

    public Map<String, Double> calcularTodos(Funcionario funcionario) {
        Map<String, Double> resultados = new HashMap<>();

        CalculoContext baseContext = CalculoContext.builder()
                .salarioBruto(funcionario.getSalarioBruto())
                .numeroDependentes(funcionario.getNumeroDependentes())
                .build();

        // 1) Calcula INSS primeiro, se disponível
        Double inssCalculado = null;
        for (ICalculo calculo : calculos) {
            if ("INSS".equalsIgnoreCase(calculo.getTipo())) {
                inssCalculado = calculo.calcular(baseContext);
                resultados.put(calculo.getTipo(), inssCalculado);
                break;
            }
        }

        // 2) Com INSS conhecido (ou não), calcula os demais
        CalculoContext contextComInss = CalculoContext.builder()
                .salarioBruto(baseContext.getSalarioBruto())
                .numeroDependentes(baseContext.getNumeroDependentes())
                .inss(inssCalculado != null ? inssCalculado : funcionario.getInss())
                .build();

        for (ICalculo calculo : calculos) {
            if ("INSS".equalsIgnoreCase(calculo.getTipo())) continue;
            double valor = calculo.calcular(contextComInss);
            resultados.put(calculo.getTipo(), valor);
        }

        return resultados;
    }
}
