CREATE TABLE IF NOT EXISTS funcionario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    cargo VARCHAR(60),
    departamento VARCHAR(60),
    data_admissao DATE,
    salario_bruto NUMERIC(12,2) NOT NULL,
    inss NUMERIC(12,2),
    fgts NUMERIC(12,2),
    irrf NUMERIC(12,2),
    salario_liquido NUMERIC(12,2),
    horas_previstas NUMERIC(10,2),
    horas_trabalhadas NUMERIC(10,2),
    vale_transporte BOOLEAN NOT NULL DEFAULT FALSE,
    numero_dependentes INTEGER NOT NULL DEFAULT 0,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);
