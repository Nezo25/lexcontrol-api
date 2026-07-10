-- V10__cleanup_tabela_clientes_faturamento.sql
-- Remove colunas de faturamento e causa da tabela de clientes, pois foram movidas para tb_contrato_honorario

ALTER TABLE clientes
DROP COLUMN causa,
DROP COLUMN status_pagamento,
DROP COLUMN valor_causa,
DROP COLUMN modelo_de_pagamento,
DROP COLUMN valor_parcela,
DROP COLUMN total_honorarios,
DROP COLUMN data_de_vencimento,
DROP COLUMN status;
