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

-- Dados de exemplo
INSERT INTO funcionario (nome, cpf, cargo, departamento, data_admissao, salario_bruto, inss, fgts, irrf, salario_liquido, horas_previstas, horas_trabalhadas, vale_transporte, numero_dependentes, ativo, matricula, senha, is_admin, alimentacao)
VALUES 
('Administrador', '000.000.000-00', 'Gerente', 'Administrativo', '2020-01-01', 8000.00, 640.00, 640.00, 450.00, 6910.00, 220, 220, TRUE, 0, TRUE, '0001', '$2a$10$abcdefghijklmnopqrstuv', TRUE, TRUE),
('João Silva', '111.111.111-11', 'Analista', 'TI', '2021-06-15', 5000.00, 400.00, 400.00, 200.00, 4400.00, 220, 220, TRUE, 1, TRUE, '0002', '$2a$10$abcdefghijklmnopqrstuv', FALSE, TRUE),
('Maria Santos', '222.222.222-22', 'Desenvolvedora', 'TI', '2022-03-10', 6500.00, 520.00, 520.00, 300.00, 5680.00, 220, 220, TRUE, 2, TRUE, '0003', '$2a$10$abcdefghijklmnopqrstuv', FALSE, TRUE),
('Pedro Costa', '333.333.333-33', 'Assistente', 'RH', '2023-01-20', 3500.00, 280.00, 280.00, 80.00, 3140.00, 220, 220, FALSE, 0, TRUE, '0004', '$2a$10$abcdefghijklmnopqrstuv', FALSE, FALSE)
ON CONFLICT (matricula) DO NOTHING;

-- Folhas de pagamento de exemplo
INSERT INTO folha_pagamento (funcionario_id, mes_referencia, salario_bruto, salario_liquido, total_descontos, total_beneficios)
SELECT 
    f.id,
    '2024-11-01'::DATE,
    f.salario_bruto,
    f.salario_liquido,
    (f.inss + f.irrf),
    CASE WHEN f.vale_transporte THEN 200.00 ELSE 0 END + CASE WHEN f.alimentacao THEN 300.00 ELSE 0 END
FROM funcionario f
WHERE f.ativo = TRUE
ON CONFLICT DO NOTHING;

INSERT INTO folha_pagamento (funcionario_id, mes_referencia, salario_bruto, salario_liquido, total_descontos, total_beneficios)
SELECT 
    f.id,
    '2024-10-01'::DATE,
    f.salario_bruto,
    f.salario_liquido,
    (f.inss + f.irrf),
    CASE WHEN f.vale_transporte THEN 200.00 ELSE 0 END + CASE WHEN f.alimentacao THEN 300.00 ELSE 0 END
FROM funcionario f
WHERE f.ativo = TRUE
ON CONFLICT DO NOTHING;

INSERT INTO folha_pagamento (funcionario_id, mes_referencia, salario_bruto, salario_liquido, total_descontos, total_beneficios)
SELECT 
    f.id,
    '2024-12-01'::DATE,
    f.salario_bruto,
    f.salario_liquido,
    (f.inss + f.irrf),
    CASE WHEN f.vale_transporte THEN 200.00 ELSE 0 END + CASE WHEN f.alimentacao THEN 300.00 ELSE 0 END
FROM funcionario f
WHERE f.ativo = TRUE
ON CONFLICT DO NOTHING;
