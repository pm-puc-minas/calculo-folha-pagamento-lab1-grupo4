package com.trabalho.FolhaPag.modules.funcionario.service;

import com.trabalho.FolhaPag.Exceptions.UnexpectedException;
import com.trabalho.FolhaPag.Exceptions.ValueNotValidException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.trabalho.FolhaPag.modules.funcionario.entity.Funcionario;
import com.trabalho.FolhaPag.modules.funcionario.dto.FuncionarioDTO;
import com.trabalho.FolhaPag.modules.funcionario.repository.FuncionarioRepository;
import com.trabalho.FolhaPag.modules.folha.service.calculo.factory.ImpostoCalculadorFactory;
import com.trabalho.FolhaPag.modules.folha.service.calculo.factory.TipoImposto;
import com.trabalho.FolhaPag.modules.folha.service.calculo.model.CalculoContext;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepository repository;
    private final ImpostoCalculadorFactory impostoFactory;

    public List<Funcionario> listarTodos() {
        return repository.findAll();
    }

    public Optional<Funcionario> buscarPorId(Long id) {
        if (id == null) return Optional.empty();
        return repository.findById(id);
    }

    public Funcionario buscarPorMatricula(String matricula) {
        if (matricula == null) return null;
        return repository.findByMatricula(matricula);
    }

    public List<Funcionario> listarVisiveisPara(String requesterMatricula) {
        Funcionario req = buscarPorMatricula(requesterMatricula);
        if (req == null) {
            // sem header, por segurança não expor todos
            return List.of();
        }
        if (req.getIsAdmin() || "0001".equals(req.getMatricula())) {
            return listarTodos();
        }
        return List.of(req);
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
                .matricula(dto.getMatricula())
                .cargo(dto.getCargo())
                .departamento(dto.getDepartamento())
                .salarioBruto(dto.getSalarioBruto())
                .dataAdmissao(dto.getDataAdmissao())
                .horasPrevistas(dto.getHorasPrevistas())
                .horasTrabalhadas(dto.getHorasTrabalhadas())
                .valeTransporte(dto.getValeTransporte())
                .alimentacao(dto.getAlimentacao()) 
                .numeroDependentes(dto.getNumeroDependentes())
                .ativo(true)
                .isAdmin(false)
                .build();
    }

    private void aplicarCalculos(Funcionario f) {
        CalculoContext baseContext = CalculoContext.builder()
                .salarioBruto(f.getSalarioBruto())
                .numeroDependentes(f.getNumeroDependentes())
                .build();

        double inss = impostoFactory.obterCalculador(TipoImposto.INSS)
                .calcular(baseContext);

        CalculoContext contextComInss = CalculoContext.builder()
                .salarioBruto(f.getSalarioBruto())
                .numeroDependentes(f.getNumeroDependentes())
                .inss(inss)
                .build();

        double irrf = impostoFactory.obterCalculador(TipoImposto.IRRF)
                .calcular(contextComInss);
        double fgts = impostoFactory.obterCalculador(TipoImposto.FGTS)
                .calcular(baseContext);

        f.setInss(inss);
        f.setIrrf(irrf);
        f.setFgts(fgts);
        f.setSalarioLiquido(f.getSalarioBruto() - inss - irrf);
    }
}
