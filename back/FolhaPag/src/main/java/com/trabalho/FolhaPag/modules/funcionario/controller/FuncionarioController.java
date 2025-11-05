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
    public List<Funcionario> listar() {
        return service.listarTodos();
    }

    @PostMapping
    public ResponseEntity<Funcionario> criar(@RequestBody FuncionarioDTO dto) {
        Funcionario salvo = service.criar(dto);
        return ResponseEntity.ok(salvo);
    }
}
