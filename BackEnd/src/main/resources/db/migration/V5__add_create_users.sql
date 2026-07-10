-- ============================================================
-- V5__create_users_and_refresh_tokens.sql
-- LexControl — Módulo de Autenticação JWT
-- ============================================================

CREATE TABLE IF NOT EXISTS tb_usuario (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    nome          VARCHAR(100) NOT NULL,
    email         VARCHAR(150) NOT NULL UNIQUE,
    senha         VARCHAR(255) NOT NULL,
    role          ENUM('ADMIN','ADVOGADO','ASSISTENTE') NOT NULL DEFAULT 'ASSISTENTE',
    ativo         BOOLEAN      NOT NULL DEFAULT TRUE,
    criado_em     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_tb_usuario PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tb_refresh_token (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    token       VARCHAR(512) NOT NULL UNIQUE,
    usuario_id  BIGINT       NOT NULL,
    expira_em   DATETIME     NOT NULL,
    revogado    BOOLEAN      NOT NULL DEFAULT FALSE,
    criado_em   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_tb_refresh_token PRIMARY KEY (id),
    CONSTRAINT fk_refresh_usuario FOREIGN KEY (usuario_id)
        REFERENCES tb_usuario (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_usuario_email   ON tb_usuario (email);
CREATE INDEX idx_refresh_token   ON tb_refresh_token (token);
CREATE INDEX idx_refresh_usuario ON tb_refresh_token (usuario_id);

-- Usuário admin inicial (senha: Admin@1234)
INSERT INTO tb_usuario (nome, email, senha, role, ativo)
VALUES (
    'Administrador',
    'admin@lexcontrol.com.br',
    '$2a$12$92CntDmPf7rJwWouSnfbHuZDPV3yELPiamVkMSjYoNWpYBkKJLNny',
    'ADMIN',
    true
);