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
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    matricula VARCHAR(10) UNIQUE NOT NULL,
    senha VARCHAR(255),
    is_admin BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS folha_pagamento (
    id SERIAL PRIMARY KEY,
    funcionario_id BIGINT NOT NULL REFERENCES funcionario(id) ON DELETE CASCADE,
    mes_referencia DATE NOT NULL,
    salario_bruto NUMERIC(12,2) NOT NULL,
    salario_liquido NUMERIC(12,2),
    total_descontos NUMERIC(12,2) DEFAULT 0,
    total_beneficios NUMERIC(12,2) DEFAULT 0,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS desconto (
    id SERIAL PRIMARY KEY,
    folha_pagamento_id BIGINT NOT NULL REFERENCES folha_pagamento(id) ON DELETE CASCADE,
    nome VARCHAR(80) NOT NULL,
    valor NUMERIC(12,2) NOT NULL,
    tipo VARCHAR(40) DEFAULT 'INSS' 
);

CREATE TABLE IF NOT EXISTS beneficio (
    id SERIAL PRIMARY KEY,
    folha_pagamento_id BIGINT NOT NULL REFERENCES folha_pagamento(id) ON DELETE CASCADE,
    nome VARCHAR(80) NOT NULL,
    valor NUMERIC(12,2) NOT NULL,
    tipo VARCHAR(40) DEFAULT 'VALE_TRANSPORTE'  
);

CREATE TABLE IF NOT EXISTS adicional (
    id SERIAL PRIMARY KEY,
    folha_pagamento_id BIGINT NOT NULL REFERENCES folha_pagamento(id) ON DELETE CASCADE,
    tipo VARCHAR(40) NOT NULL,  
    percentual NUMERIC(5,2) DEFAULT 0,
    valor NUMERIC(12,2) NOT NULL
);
