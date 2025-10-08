package com.trabalho.FolhaPag.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalculoService {

    private final List<ICalculo> calculos;

    public Map<String, Double> calcularTodos(double salario) {
        return calculos.stream()
                .collect(Collectors.toMap(
                        ICalculo::getNome,
                        c -> c.calcular(salario)
                ));
    }
}
