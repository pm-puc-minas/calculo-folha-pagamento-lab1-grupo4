package com.trabalho.FolhaPag.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.trabalho.FolhaPag.entity.Funcionario;
import com.trabalho.FolhaPag.dto.FuncionarioDTO;
import com.trabalho.FolhaPag.repository.FuncionarioRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepository repository;

    public List<Funcionario> listarTodos() {
        return repository.findAll();
    }

    public Funcionario criar(FuncionarioDTO dto) {
        Funcionario funcionario = converterParaEntidade(dto);
        calcularDescontos(funcionario);
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

    private void calcularDescontos(Funcionario f) {
        double inss = f.getSalarioBruto() * 0.075;
        double fgts = f.getSalarioBruto() * 0.08;
        double irrf = f.getSalarioBruto() > 2500 ? f.getSalarioBruto() * 0.15 : 0;
        double salarioLiquido = f.getSalarioBruto() - inss - irrf;

        f.setInss(inss);
        f.setFgts(fgts);
        f.setIrrf(irrf);
        f.setSalarioLiquido(salarioLiquido);
    }
}
