package com.trabalho.FolhaPag.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;

import com.trabalho.FolhaPag.dto.FuncionarioDTO;
import com.trabalho.FolhaPag.entity.Funcionario;
import com.trabalho.FolhaPag.service.FuncionarioService;

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
