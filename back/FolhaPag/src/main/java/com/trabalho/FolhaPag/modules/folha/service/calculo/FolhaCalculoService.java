package com.trabalho.FolhaPag.modules.folha.service.calculo;

import com.trabalho.FolhaPag.modules.folha.service.calculo.interfaces.ICalculo;
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

        for (ICalculo calculo : calculos) {
            double valor = calculo.calcular(funcionario);
            resultados.put(calculo.getTipo(), valor);
        }

        return resultados;
    }
}
