package com.trabalho.FolhaPag.modules.folha.controller;

import com.trabalho.FolhaPag.modules.folha.entity.FolhaPagamento;
import com.trabalho.FolhaPag.modules.folha.service.FolhaPagamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

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
    private final com.trabalho.FolhaPag.modules.funcionario.service.FuncionarioService funcionarioService;

    @GetMapping
    public ResponseEntity<List<FolhaPagamento>> listarTodas(@RequestHeader(value = "X-User-Matricula", required = false) String requesterMatricula) {
        var requester = funcionarioService.buscarPorMatricula(requesterMatricula);
        if (requester == null) {
            return ResponseEntity.status(401).build();
        }
        if (requester.getIsAdmin() || "0001".equals(requester.getMatricula())) {
            return ResponseEntity.ok(service.listarTodas());
        }
        return ResponseEntity.ok(service.listarPorMatricula(requester.getMatricula()));
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
    public ResponseEntity<byte[]> exportarPdf(@RequestHeader(value = "X-User-Matricula", required = false) String requesterMatricula) {
        try {
            var requester = funcionarioService.buscarPorMatricula(requesterMatricula);
            if (requester == null) {
                return ResponseEntity.status(401).build();
            }
            List<FolhaPagamento> folhas;
            if (requester.getIsAdmin() || "0001".equals(requester.getMatricula())) {
                folhas = service.listarTodas();
            } else {
                folhas = service.listarPorMatricula(requester.getMatricula());
            }

            try (PDDocument doc = new PDDocument()) {
                final float margin = 40f;
                final PDRectangle pageSize = PDRectangle.LETTER;
                final float yStartOffset = 80f;
                final float rowHeight = 16f;
                final float headerFontSize = 12f;
                final float cellFontSize = 10f;

                // column widths: ID, Nome, Matrícula, Período, Salário
                float[] colWidths = new float[] {40f, 200f, 90f, 80f, 90f};

                int pageNumber = 0;
                float yPosition = 0f;

                NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                PDPage page = null;
                PDPageContentStream content = null;

                for (int i = 0; i < folhas.size(); i++) {
                    if (page == null) {
                        page = new PDPage(pageSize);
                        doc.addPage(page);
                        pageNumber++;
                        content = new PDPageContentStream(doc, page);

                        // Title
                        content.beginText();
                        content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                        content.newLineAtOffset(margin, pageSize.getHeight() - margin);
                        content.showText("Folhas de Pagamento");
                        content.endText();

                        // Date
                        content.beginText();
                        content.setFont(PDType1Font.HELVETICA, 10);
                        content.newLineAtOffset(pageSize.getWidth() - margin - 120, pageSize.getHeight() - margin + 2);
                        content.showText("Data: " + LocalDate.now().format(dateFmt));
                        content.endText();

                        // Column headers
                        yPosition = pageSize.getHeight() - margin - yStartOffset;
                        float x = margin;
                        content.setFont(PDType1Font.HELVETICA_BOLD, headerFontSize);
                        content.beginText();
                        content.newLineAtOffset(x, yPosition);
                        for (int c = 0; c < colWidths.length; c++) {
                            String h = switch (c) {
                                case 0 -> "ID";
                                case 1 -> "Funcionário";
                                case 2 -> "Matrícula";
                                case 3 -> "Período";
                                default -> "Salário";
                            };
                            content.showText(h);
                            content.newLineAtOffset(colWidths[c], 0);
                        }
                        content.endText();
                        yPosition -= (rowHeight + 6);
                        content.setFont(PDType1Font.HELVETICA, cellFontSize);
                    }

                    FolhaPagamento f = folhas.get(i);

                    if (yPosition < margin + 40) {
                        // footer
                        content.beginText();
                        content.setFont(PDType1Font.HELVETICA_OBLIQUE, 9);
                        content.newLineAtOffset(margin, margin - 10);
                        content.showText("Página " + pageNumber);
                        content.endText();

                        content.close();
                        page = new PDPage(pageSize);
                        doc.addPage(page);
                        pageNumber++;
                        content = new PDPageContentStream(doc, page);

                        // header on new page
                        content.beginText();
                        content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                        content.newLineAtOffset(margin, pageSize.getHeight() - margin);
                        content.showText("Folhas de Pagamento");
                        content.endText();

                        content.beginText();
                        content.setFont(PDType1Font.HELVETICA, 10);
                        content.newLineAtOffset(pageSize.getWidth() - margin - 120, pageSize.getHeight() - margin + 2);
                        content.showText("Data: " + LocalDate.now().format(dateFmt));
                        content.endText();

                        // column headers again
                        yPosition = pageSize.getHeight() - margin - yStartOffset;
                        float xh = margin;
                        content.setFont(PDType1Font.HELVETICA_BOLD, headerFontSize);
                        content.beginText();
                        content.newLineAtOffset(xh, yPosition);
                        for (int c = 0; c < colWidths.length; c++) {
                            String h = switch (c) {
                                case 0 -> "ID";
                                case 1 -> "Funcionário";
                                case 2 -> "Matrícula";
                                case 3 -> "Período";
                                default -> "Salário";
                            };
                            content.showText(h);
                            content.newLineAtOffset(colWidths[c], 0);
                        }
                        content.endText();
                        yPosition -= (rowHeight + 6);
                        content.setFont(PDType1Font.HELVETICA, cellFontSize);
                    }

                    // draw the row
                    float xPos = margin;
                    content.beginText();
                    content.newLineAtOffset(xPos, yPosition);
                    content.showText(String.valueOf(f.getId()));
                    content.newLineAtOffset(colWidths[0], 0);
                    String nome = f.getFuncionario() != null ? f.getFuncionario().getNome() : "-";
                    content.showText(truncate(nome, 30));
                    content.newLineAtOffset(colWidths[1], 0);
                    String matricula = f.getFuncionario() != null ? f.getFuncionario().getMatricula() : "-";
                    content.showText(matricula);
                    content.newLineAtOffset(colWidths[2], 0);
                    String periodo = f.getMesReferencia() != null ? f.getMesReferencia().toString() : "-";
                    content.showText(periodo);
                    content.newLineAtOffset(colWidths[3], 0);
                    Double salario = f.getSalarioLiquido() != null ? f.getSalarioLiquido() : f.getSalarioBruto();
                    content.showText(salario != null ? currency.format(salario) : "-");
                    content.endText();

                    yPosition -= rowHeight;
                }

                // write footer on last open content/page
                if (content != null) {
                    content.beginText();
                    content.setFont(PDType1Font.HELVETICA_OBLIQUE, 9);
                    content.newLineAtOffset(margin, margin - 10);
                    content.showText("Página " + pageNumber);
                    content.endText();
                    content.close();
                }

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
