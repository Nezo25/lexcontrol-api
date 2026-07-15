-- Adiciona índices para isolamento de escrita (evitar table scans no Extrato)
CREATE INDEX idx_extrato_comissao_fatura ON tb_extrato_comissao (fatura_id);
CREATE INDEX idx_extrato_comissao_advogado ON tb_extrato_comissao (advogado_id);

-- Índice para a busca rápida do tribunal por CNJ
CREATE INDEX idx_processo_cnj ON tb_processo (cnj);

-- Índice para acelerar a query nativa do BI por área
CREATE INDEX idx_processo_area ON tb_processo (area_direito);
