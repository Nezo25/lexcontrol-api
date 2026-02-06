CREATE TABLE cliente_advogado (
    cliente_id BIGINT NOT NULL,
    advogado_id BIGINT NOT NULL,
    PRIMARY KEY (cliente_id, advogado_id),
    CONSTRAINT fk_link_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    CONSTRAINT fk_link_advogado FOREIGN KEY (advogado_id) REFERENCES advogados(id_advogado)
);