
/* teste no adminner */ 

DO
$$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'folha_user') THEN
        CREATE ROLE folha_user WITH LOGIN PASSWORD 'SenhaSegura123';
    END IF;
END
$$;

DO
$$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'folha') THEN
        CREATE DATABASE folha OWNER folha_user;
    END IF;
END
$$;

\connect folha
/* deu bom */ 
CREATE TABLE IF NOT EXISTS funcionario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) UNIQUE,
    salario NUMERIC(15,2) NOT NULL
);
