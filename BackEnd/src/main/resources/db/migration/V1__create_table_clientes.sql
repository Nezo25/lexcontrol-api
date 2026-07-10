CREATE TABLE clientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_cliente VARCHAR(150) NOT NULL,
    telefone VARCHAR(20),
    causa TEXT,
    status_pagamento VARCHAR(50),
    valor_causa DECIMAL(10, 2),
    cpf VARCHAR(14) NOT NULL,
    rg VARCHAR(20) NOT NULL,
    data_de_vencimento DATE NOT NULL,
    modelo_de_pagamento VARCHAR(50),
    valor_parcela DECIMAL(10, 2),
    total_honorarios DECIMAL(10, 2),

    -- Endereço (Embedded)
    logradouro VARCHAR(150),
    numero VARCHAR(20),
    complemento VARCHAR(100),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    estado CHAR(2),
    cep VARCHAR(9)
);