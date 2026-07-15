CREATE TABLE tb_processo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cnj VARCHAR(30) NOT NULL UNIQUE,
    area_direito VARCHAR(100),
    contrato_id BIGINT NOT NULL,
    CONSTRAINT fk_processo_contrato FOREIGN KEY (contrato_id) REFERENCES tb_contrato_honorario(id) ON DELETE CASCADE
);

CREATE TABLE tb_regra_rateio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    contrato_id BIGINT NOT NULL,
    advogado_id BIGINT NOT NULL,
    percentual_comissao DECIMAL(5,2) NOT NULL,
    CONSTRAINT fk_regrarateio_contrato FOREIGN KEY (contrato_id) REFERENCES tb_contrato_honorario(id) ON DELETE CASCADE,
    CONSTRAINT fk_regrarateio_advogado FOREIGN KEY (advogado_id) REFERENCES advogados(id_advogado) ON DELETE CASCADE
);

CREATE TABLE tb_extrato_comissao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fatura_id BIGINT NOT NULL,
    advogado_id BIGINT NOT NULL,
    valor_receber DECIMAL(15,2) NOT NULL,
    status_pagamento VARCHAR(30) NOT NULL DEFAULT 'PENDENTE',
    CONSTRAINT fk_extrato_fatura FOREIGN KEY (fatura_id) REFERENCES tb_fatura(id) ON DELETE CASCADE,
    CONSTRAINT fk_extrato_advogado FOREIGN KEY (advogado_id) REFERENCES advogados(id_advogado) ON DELETE CASCADE
);
