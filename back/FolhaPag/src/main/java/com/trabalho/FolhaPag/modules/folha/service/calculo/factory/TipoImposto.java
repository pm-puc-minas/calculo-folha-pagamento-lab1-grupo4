package com.trabalho.FolhaPag.modules.folha.service.calculo.factory;

public enum TipoImposto {
    INSS("INSS", "Instituto Nacional do Seguro Social"),
    IRRF("IRRF", "Imposto de Renda Retido na Fonte"),
    FGTS("FGTS", "Fundo de Garantia do Tempo de Serviço");

    private final String codigo;
    private final String descricao;

    TipoImposto(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoImposto fromCodigo(String codigo) {
        for (TipoImposto tipo : values()) {
            if (tipo.codigo.equalsIgnoreCase(codigo)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de imposto não encontrado: " + codigo);
    }
}
