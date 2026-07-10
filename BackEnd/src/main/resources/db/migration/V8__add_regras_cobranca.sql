-- Adicionando colunas de regras financeiras e metadados de sucumbência
ALTER TABLE tb_contrato_honorario 
ADD COLUMN descricao_cobranca VARCHAR(255) NOT NULL,
ADD COLUMN tipo_honorario VARCHAR(50) NOT NULL,
ADD COLUMN frequencia VARCHAR(50),
ADD COLUMN data_termino_cobranca DATE,
ADD COLUMN percentual_juros_mes DECIMAL(5, 2) DEFAULT 0.00,
ADD COLUMN percentual_multa DECIMAL(5, 2) DEFAULT 0.00,
ADD COLUMN multa_valor_fixo BOOLEAN DEFAULT FALSE,
ADD COLUMN nome_pagador_sucumbencia VARCHAR(150),
ADD COLUMN documento_pagador_sucumbencia VARCHAR(20);

-- Tabela auxiliar para o Set de Canais de Notificação (@ElementCollection)
CREATE TABLE tb_contrato_canais_notificacao (
    contrato_id BIGINT NOT NULL,
    canal_notificacao VARCHAR(50) NOT NULL,
    PRIMARY KEY (contrato_id, canal_notificacao),
    CONSTRAINT fk_canais_contrato FOREIGN KEY (contrato_id) REFERENCES tb_contrato_honorario(id) ON DELETE CASCADE
);
