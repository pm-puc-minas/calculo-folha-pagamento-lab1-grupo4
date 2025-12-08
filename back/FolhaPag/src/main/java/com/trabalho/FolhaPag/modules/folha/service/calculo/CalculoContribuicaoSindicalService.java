package com.trabalho.FolhaPag.modules.folha.service.calculo;

import com.trabalho.FolhaPag.modules.folha.service.calculo.interfaces.ICalculo;
import com.trabalho.FolhaPag.modules.folha.service.calculo.model.CalculoContext;
import org.springframework.stereotype.Service;

/**
 * EXEMPLO de como adicionar um novo imposto usando o Factory Pattern.
 * 
 * Para adicionar um novo imposto ao sistema:
 * 
 * 1. Criar uma classe que implementa ICalculo (como esta)
 * 2. Adicionar o novo tipo em TipoImposto.java:
 *    CONTRIBUICAO_SINDICAL("CS", "Contribuição Sindical")
 * 
 * 3. Registrar no ImpostoCalculadorFactory.java:
 *    calculadores.put(TipoImposto.CONTRIBUICAO_SINDICAL, new CalculoContribuicaoSindicalService());
 * 
 * Pronto! O novo imposto estará disponível em todo o sistema.
 */
@Service
public class CalculoContribuicaoSindicalService implements ICalculo {

    @Override
    public double calcular(CalculoContext context) {
        // Exemplo: 1% do salário bruto
        return context.getSalarioBruto() * 0.01;
    }

    @Override
    public String getTipo() {
        return "CONTRIBUICAO_SINDICAL";
    }
}
