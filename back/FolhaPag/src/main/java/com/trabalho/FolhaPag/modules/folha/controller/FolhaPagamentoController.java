package com.trabalho.FolhaPag.modules.folha.controller;

import com.trabalho.FolhaPag.modules.folha.entity.FolhaPagamento;
import com.trabalho.FolhaPag.modules.folha.service.FolhaPagamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

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

    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportarPdf() {
        try {
            List<FolhaPagamento> folhas = service.listarTodas();

            try (PDDocument doc = new PDDocument()) {
                PDPage page = new PDPage(PDRectangle.LETTER);
                doc.addPage(page);

                PDPageContentStream content = new PDPageContentStream(doc, page);

                // Título
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.newLineAtOffset(40, page.getMediaBox().getHeight() - 40);
                content.showText("Folhas de Pagamento");
                content.endText();

                float y = page.getMediaBox().getHeight() - 70;
                content.setFont(PDType1Font.HELVETICA_BOLD, 10);
                content.beginText();
                content.newLineAtOffset(40, y);
                content.showText("ID");
                content.newLineAtOffset(40, 0);
                content.showText("Funcionario");
                content.newLineAtOffset(140, 0);
                content.showText("Matricula");
                content.newLineAtOffset(80, 0);
                content.showText("Periodo");
                content.newLineAtOffset(80, 0);
                content.showText("Salario Liquido");
                content.endText();

                y -= 18;
                content.setFont(PDType1Font.HELVETICA, 10);

                for (FolhaPagamento f : folhas) {
                    if (y < 60) {
                        content.close();
                        page = new PDPage(PDRectangle.LETTER);
                        doc.addPage(page);
                        content = new PDPageContentStream(doc, page);
                        y = page.getMediaBox().getHeight() - 40;
                    }

                    content.beginText();
                    content.newLineAtOffset(40, y);
                    String idText = String.valueOf(f.getId());
                    content.showText(idText);
                    content.newLineAtOffset(40, 0);
                    String nome = f.getFuncionario() != null ? f.getFuncionario().getNome() : "-";
                    content.showText(truncate(nome, 30));
                    content.newLineAtOffset(140, 0);
                    String matricula = f.getFuncionario() != null ? f.getFuncionario().getMatricula() : "-";
                    content.showText(matricula);
                    content.newLineAtOffset(80, 0);
                    String periodo = f.getMesReferencia() != null ? f.getMesReferencia().toString() : "-";
                    content.showText(periodo);
                    content.newLineAtOffset(80, 0);
                    Double salario = f.getSalarioLiquido() != null ? f.getSalarioLiquido() : f.getSalarioBruto();
                    content.showText(salario != null ? String.format("R$ %.2f", salario) : "-");
                    content.endText();

                    y -= 16;
                }

                content.close();

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                doc.save(out);

                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=folhas.pdf");

                return ResponseEntity.ok()
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(out.toByteArray());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    private String truncate(String s, int max) {
        if (s == null) return "";
        if (s.length() <= max) return s;
        return s.substring(0, max - 3) + "...";
    }
}
