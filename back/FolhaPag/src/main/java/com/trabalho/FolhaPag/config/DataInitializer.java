package com.trabalho.FolhaPag.config;

import com.trabalho.FolhaPag.modules.funcionario.entity.Funcionario;
import com.trabalho.FolhaPag.modules.funcionario.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.security.MessageDigest;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final FuncionarioRepository funcionarioRepository;

    @Override
    public void run(String... args) throws Exception {
        // Verifica se já existe um admin na base
        if (funcionarioRepository.findByMatricula("0001") == null) {
            // Cria o admin padrão
            String senhaEncriptada = encriptarSenha("admin");
            
            Funcionario admin = Funcionario.builder()
                    .matricula("0001")
                    .nome("Administrador")
                    .senha(senhaEncriptada)
                    .isAdmin(true)
                    .ativo(true)
                    .cpf("00000000000000")
                    .cargo("Administrador")
                    .departamento("Administrativo")
                    .dataAdmissao(LocalDate.now())
                    .salarioBruto(0.0)
                    .inss(0.0)
                    .fgts(0.0)
                    .irrf(0.0)
                    .salarioLiquido(0.0)
                    .valeTransporte(false)
                    .numeroDependentes(0)
                    .build();

            funcionarioRepository.save(admin);
            System.out.println("✓ Usuário administrador criado com sucesso!");
            System.out.println("  Matrícula: 0001");
            System.out.println("  Senha: admin");
        }
    }

    private String encriptarSenha(String senha) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(senha.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }
}
