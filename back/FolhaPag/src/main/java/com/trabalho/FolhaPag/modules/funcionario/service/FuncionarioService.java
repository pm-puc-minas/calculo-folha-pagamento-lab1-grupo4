package com.trabalho.FolhaPag.modules.funcionario.service;

import com.trabalho.FolhaPag.Exceptions.UnexpectedException;
import com.trabalho.FolhaPag.Exceptions.ValueNotValidException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.trabalho.FolhaPag.modules.funcionario.entity.Funcionario;
import com.trabalho.FolhaPag.modules.funcionario.dto.FuncionarioDTO;
import com.trabalho.FolhaPag.modules.funcionario.repository.FuncionarioRepository;
import com.trabalho.FolhaPag.modules.folha.service.calculo.CalculoINSSService;
import com.trabalho.FolhaPag.modules.folha.service.calculo.CalculoIRRFService;
import com.trabalho.FolhaPag.modules.folha.service.calculo.CalculoFGTSService;
import com.trabalho.FolhaPag.modules.folha.service.calculo.model.CalculoContext;

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
        if (dto == null) {
            throw new UnexpectedException("Funcionário não pode ser nulo");
        }
        if (dto.getNome() == null) {
            throw new ValueNotValidException("Nome do funcionário inválido");
        }
        if (dto.getSalarioBruto() <= 0) {
            throw new ValueNotValidException("Salário do funcionário inválido");
        }
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
    CalculoContext baseContext = CalculoContext.builder()
                .salarioBruto(f.getSalarioBruto())
                .numeroDependentes(f.getNumeroDependentes())
                .build();

        double inss = calculoINSS.calcular(baseContext);

    CalculoContext contextComInss = CalculoContext.builder()
                .salarioBruto(f.getSalarioBruto())
                .numeroDependentes(f.getNumeroDependentes())
                .inss(inss)
                .build();

        double irrf = calculoIRRF.calcular(contextComInss);
        double fgts = calculoFGTS.calcular(baseContext);

        f.setInss(inss);
        f.setIrrf(irrf);
        f.setFgts(fgts);
        f.setSalarioLiquido(f.getSalarioBruto() - inss - irrf);
    }
}
