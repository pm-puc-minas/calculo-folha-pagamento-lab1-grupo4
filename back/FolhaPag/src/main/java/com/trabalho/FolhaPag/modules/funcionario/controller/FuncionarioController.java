package com.trabalho.FolhaPag.modules.funcionario.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;

import com.trabalho.FolhaPag.modules.funcionario.dto.FuncionarioDTO;
import com.trabalho.FolhaPag.modules.funcionario.entity.Funcionario;
import com.trabalho.FolhaPag.modules.funcionario.service.FuncionarioService;

import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
@RequiredArgsConstructor
public class FuncionarioController {

    private final FuncionarioService service;

    @GetMapping
    public List<Funcionario> listar(@RequestHeader(value = "X-User-Matricula", required = false) String requesterMatricula) {
        return service.listarVisiveisPara(requesterMatricula);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> buscarPorId(@PathVariable Long id,
                                                   @RequestHeader(value = "X-User-Matricula", required = false) String requesterMatricula) {
        Funcionario requester = service.buscarPorMatricula(requesterMatricula);
        return service.listarTodos().stream()
                .filter(f -> f.getId().equals(id))
                .findFirst()
                .map(f -> {
                    if (requester == null) {
                        return ResponseEntity.status(401).build();
                    }
                    if (requester.getIsAdmin() || "0001".equals(requester.getMatricula()) || requester.getId().equals(f.getId())) {
                        return ResponseEntity.ok(f);
                    }
                    return ResponseEntity.status(403).build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/matricula/{matricula}")
    public ResponseEntity<Funcionario> buscarPorMatricula(@PathVariable String matricula,
                                                          @RequestHeader(value = "X-User-Matricula", required = false) String requesterMatricula) {
        Funcionario requester = service.buscarPorMatricula(requesterMatricula);
        Funcionario f = service.buscarPorMatricula(matricula);
        if (f == null) return ResponseEntity.notFound().build();
        if (requester == null) return ResponseEntity.status(401).build();
        if (requester.getIsAdmin() || "0001".equals(requester.getMatricula()) || requester.getMatricula().equals(matricula)) {
            return ResponseEntity.ok(f);
        }
        return ResponseEntity.status(403).build();
    }

    @PostMapping
    public ResponseEntity<Funcionario> criar(@RequestHeader(value = "X-User-Matricula", required = false) String requesterMatricula,
                                             @RequestBody FuncionarioDTO dto) {
        Funcionario requester = service.buscarPorMatricula(requesterMatricula);
        if (requester == null) return ResponseEntity.status(401).build();
        if (!(requester.getIsAdmin() || "0001".equals(requester.getMatricula()))) {
            return ResponseEntity.status(403).build();
        }
        Funcionario salvo = service.criar(dto);
        return ResponseEntity.ok(salvo);
    }
}
