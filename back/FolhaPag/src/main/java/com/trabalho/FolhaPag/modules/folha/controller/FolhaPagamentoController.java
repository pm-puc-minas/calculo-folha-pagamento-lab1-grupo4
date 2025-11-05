package com.trabalho.FolhaPag.modules.folha.controller;

import com.trabalho.FolhaPag.modules.folha.entity.FolhaPagamento;
import com.trabalho.FolhaPag.modules.folha.service.FolhaPagamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/folhas")
@RequiredArgsConstructor
public class FolhaPagamentoController {

    private final FolhaPagamentoService service;

    @GetMapping
    public ResponseEntity<List<FolhaPagamento>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FolhaPagamento> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FolhaPagamento> criarFolha(
            @RequestParam Long funcionarioId,
            @RequestParam String mesReferencia) {

        LocalDate mes = LocalDate.parse(mesReferencia);
        FolhaPagamento nova = service.criar(funcionarioId, mes);
        return ResponseEntity.ok(nova);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
