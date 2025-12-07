package com.trabalho.FolhaPag.modules.funcionario.controller;

import com.trabalho.FolhaPag.modules.funcionario.dto.LoginDTO;
import com.trabalho.FolhaPag.modules.funcionario.dto.LoginResponseDTO;
import com.trabalho.FolhaPag.modules.funcionario.entity.Funcionario;
import com.trabalho.FolhaPag.modules.funcionario.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.util.Base64;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final FuncionarioRepository funcionarioRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            // Busca o funcionário pela matrícula
            Funcionario funcionario = funcionarioRepository.findByMatricula(loginDTO.getMatricula());

            if (funcionario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(LoginResponseDTO.builder()
                                .mensagem("Matrícula ou senha incorreta")
                                .build());
            }

            // Verifica se o funcionário está ativo
            if (!funcionario.getAtivo()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(LoginResponseDTO.builder()
                                .mensagem("Usuário inativo")
                                .build());
            }

            // Encripta a senha fornecida e compara
            String senhaEncriptada = encriptarSenha(loginDTO.getSenha());
            if (!senhaEncriptada.equals(funcionario.getSenha())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(LoginResponseDTO.builder()
                                .mensagem("Matrícula ou senha incorreta")
                                .build());
            }

            // Login bem-sucedido
            LoginResponseDTO response = LoginResponseDTO.builder()
                    .id(funcionario.getId())
                    .matricula(funcionario.getMatricula())
                    .nome(funcionario.getNome())
                    .isAdmin(funcionario.getIsAdmin())
                    .mensagem("Login realizado com sucesso!")
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(LoginResponseDTO.builder()
                            .mensagem("Erro ao processar login")
                            .build());
        }
    }

    private String encriptarSenha(String senha) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(senha.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }

    @PostMapping("/registrar")
public ResponseEntity<?> registrar(@RequestBody LoginDTO loginDTO) {
    try {
        // Busca funcionário pela matrícula
        Funcionario funcionario = funcionarioRepository.findByMatricula(loginDTO.getMatricula());

        if (funcionario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(LoginResponseDTO.builder()
                            .mensagem("Matrícula não encontrada. Entre em contato com o RH.")
                            .build());
        }

        // Verifica se já tem senha cadastrada
        if (funcionario.getSenha() != null && !funcionario.getSenha().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(LoginResponseDTO.builder()
                            .mensagem("Esta matrícula já possui senha cadastrada. Use o login.")
                            .build());
        }

        // Valida senha
        if (loginDTO.getSenha() == null || loginDTO.getSenha().length() < 4) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(LoginResponseDTO.builder()
                            .mensagem("Senha deve ter no mínimo 4 caracteres")
                            .build());
        }

        // Cadastra a senha
        String senhaEncriptada = encriptarSenha(loginDTO.getSenha());
        funcionario.setSenha(senhaEncriptada);
        funcionarioRepository.save(funcionario);

        return ResponseEntity.ok(LoginResponseDTO.builder()
                .mensagem("Senha cadastrada com sucesso! Agora você pode fazer login.")
                .nome(funcionario.getNome())
                .build());

    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(LoginResponseDTO.builder()
                        .mensagem("Erro ao cadastrar senha")
                        .build());
    }
}
}