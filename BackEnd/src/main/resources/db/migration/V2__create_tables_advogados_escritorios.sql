CREATE TABLE escritorios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_escritorio VARCHAR(150) NOT NULL,
    cnpj VARCHAR(18) UNIQUE NOT NULL,
    razao_social VARCHAR(150) NOT NULL,

    -- Endereço (Embedded)
    logradouro VARCHAR(150),
    numero VARCHAR(20),
    complemento VARCHAR(100),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    estado CHAR(2),
    cep VARCHAR(9)
);

CREATE TABLE advogados (
    id_advogado BIGINT AUTO_INCREMENT PRIMARY KEY,
    oab VARCHAR(20) NOT NULL UNIQUE,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    cnpj VARCHAR(18) UNIQUE,
    email_profissional VARCHAR(150),
    especialidade VARCHAR(50),
    data_de_cadastro DATE NOT NULL,
    escritorio_id BIGINT,

    -- Endereço (Embedded)
    logradouro VARCHAR(150),
    numero VARCHAR(20),
    complemento VARCHAR(100),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    estado CHAR(2),
    cep VARCHAR(9),

    CONSTRAINT fk_advogado_escritorio FOREIGN KEY (escritorio_id) REFERENCES escritorios(id)
);