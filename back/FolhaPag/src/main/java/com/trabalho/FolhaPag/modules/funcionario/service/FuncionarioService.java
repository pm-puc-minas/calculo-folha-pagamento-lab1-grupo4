package com.trabalho.FolhaPag.modules.funcionario.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.trabalho.FolhaPag.modules.funcionario.entity.Funcionario;
import com.trabalho.FolhaPag.modules.funcionario.dto.FuncionarioDTO;
import com.trabalho.FolhaPag.modules.funcionario.repository.FuncionarioRepository;
import com.trabalho.FolhaPag.modules.folha.service.calculo.CalculoINSSService;
import com.trabalho.FolhaPag.modules.folha.service.calculo.CalculoIRRFService;
import com.trabalho.FolhaPag.modules.folha.service.calculo.CalculoFGTSService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepository repository;
    private final CalculoINSSService calculoINSS;
    private final CalculoIRRFService calculoIRRF;
    private final CalculoFGTSService calculoFGTS;

    public List<Funcionario> listarTodos() {
        return repository.findAll();
    }

    public Funcionario criar(FuncionarioDTO dto) {
        Funcionario funcionario = converterParaEntidade(dto);
        aplicarCalculos(funcionario);
        return repository.save(funcionario);
    }

    private Funcionario converterParaEntidade(FuncionarioDTO dto) {
        return Funcionario.builder()
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .cargo(dto.getCargo())
                .departamento(dto.getDepartamento())
                .salarioBruto(dto.getSalarioBruto())
                .dataAdmissao(dto.getDataAdmissao())
                .horasPrevistas(dto.getHorasPrevistas())
                .horasTrabalhadas(dto.getHorasTrabalhadas())
                .valeTransporte(dto.getValeTransporte())
                .numeroDependentes(dto.getNumeroDependentes())
                .ativo(true)
                .build();
    }

    private void aplicarCalculos(Funcionario f) {
        double inss = calculoINSS.calcular(f);
        double irrf = calculoIRRF.calcular(f);
        double fgts = calculoFGTS.calcular(f);

        f.setInss(inss);
        f.setIrrf(irrf);
        f.setFgts(fgts);
        f.setSalarioLiquido(f.getSalarioBruto() - inss - irrf);
    }
}
