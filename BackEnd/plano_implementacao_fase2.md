# Fase 2: Expansão do Motor de Cobranças (Recorrência, Juros e Sucumbência)

Este plano detalha a evolução do Motor de Cobranças da LexControl API para suportar recorrências dinâmicas, inteligência de multas/juros e notificações multicanal, incluindo regras específicas para **Honorários de Sucumbência**.

## Resumo das Modificações

### 1. Novos Enums
Serão criados no pacote `tfs.lexcontrol_api.enums`:
*   **[NEW]** `FrequenciaCobranca.java`: MENSAL, BIMESTRAL, TRIMESTRAL, SEMESTRAL, ANUAL.
*   **[NEW]** `CanalNotificacao.java`: SMS, WHATSAPP, EMAIL.
*   **[NEW]** `TipoHonorario.java`: CONTRATUAL, SUCUMBENCIA, PRO_LABORE.

### 2. Expansão das Entidades (Models)
*   **[MODIFY]** `ContratoHonorario.java`: Adição dos campos `descricaoCobranca`, `tipoHonorario`, `frequencia`, `dataTerminoCobranca`, `percentualJurosMes`, `percentualMulta`, `multaValorFixo`.
*   **[MODIFY]** `ContratoHonorario.java` (Honorários de Sucumbência): Como a sucumbência é paga pela **parte vencida**, adicionarei os campos opcionais `nomePagadorSucumbencia` e `documentoPagadorSucumbencia` (CPF/CNPJ). Caso o tipo de honorário seja `SUCUMBENCIA`, o sistema emitirá a cobrança em nome desse terceiro e não do cliente principal.
*   **[MODIFY]** `ContratoHonorario.java`: Adição da coleção de canais de notificação (`@ElementCollection Set<CanalNotificacao> canaisNotificacao`).

### 3. Persistência (Flyway)
*   **[NEW]** `V8__add_regras_cobranca.sql`: Script de migração que fará o `ALTER TABLE tb_contrato_honorario` para adicionar as colunas financeiras, os dados do pagador de sucumbência e criará a tabela auxiliar `tb_contrato_canais_notificacao`.

### 4. Camada de Negócio (Services)
*   **[MODIFY]** `CobrancaService.java`: 
    *   **Loop Dinâmico**: O sistema calculará o vencimento iterativamente (somando meses/anos conforme a `FrequenciaCobranca`) com parada na `dataTerminoCobranca`.
    *   **Regra de Sucumbência**: Se o `tipoHonorario` for `SUCUMBENCIA`, os dados do devedor serão extraídos dos campos de sucumbência recém-criados em vez do cliente vinculado ao contrato.
*   **[NEW]** `NotificacaoCobrancaService.java`: Um novo serviço responsável por orquestrar os disparos de alertas usando o telefone/e-mail nativo da entidade `Cliente` para canais selecionados de forma idempotente.

### 5. Documentação / Memorial Técnico
*   **[MODIFY]** `memorial_tecnico.md`: O documento será atualizado com a nova seção de Governança Multicanal e Automação de Juros/Multas (já fornecido). 
*   **[NOVO PONTO]** Adicionarei também um parágrafo focado em "Triangulação de Pagamentos" justificando arquiteturalmente como o sistema gerencia Honorários de Sucumbência isolando o risco financeiro do cliente da base.
