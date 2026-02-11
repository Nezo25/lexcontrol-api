-- Adiciona a coluna de status na tabela que já existe
ALTER TABLE clientes ADD COLUMN status VARCHAR(20) DEFAULT 'PENDENTE';

-- Cria a tabela de notificações
CREATE TABLE notificacoes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mensagem VARCHAR(255) NOT NULL,
    data_notificacao DATETIME DEFAULT CURRENT_TIMESTAMP,
    lida BOOLEAN DEFAULT FALSE
);