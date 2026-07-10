CREATE TABLE tb_contrato_honorario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    processo_id BIGINT,
    valor_total DECIMAL(15, 2) NOT NULL,
    total_parcelas INT NOT NULL,
    modalidade_parcelamento VARCHAR(50) NOT NULL,
    meio_pagamento_padrao VARCHAR(30) NOT NULL,
    data_primeiro_vencimento DATE NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_contrato_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

CREATE TABLE tb_fatura (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    contrato_id BIGINT NOT NULL,
    numero_parcela VARCHAR(20) NOT NULL,
    valor DECIMAL(15, 2) NOT NULL,
    data_vencimento DATE NOT NULL,
    status_fatura VARCHAR(30) NOT NULL,
    meio_pagamento VARCHAR(30) NOT NULL,
    link_pagamento_url VARCHAR(255),
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_fatura_contrato FOREIGN KEY (contrato_id) REFERENCES tb_contrato_honorario(id) ON DELETE CASCADE
);
