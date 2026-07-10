# MEMORIAL TÉCNICO: ARQUITETURA DO MOTOR DE COBRANÇAS JURÍDICAS (LEXCONTROL)

## 1. Sumário Executivo
Este documento detalha a fundamentação arquitetural e de engenharia de software aplicada no desenvolvimento do Motor de Cobranças Multimodal da **LexControl API**. O sistema foi concebido para atender às demandas de faturamento de um escritório de advocacia, unindo o rigor fiscal com a flexibilidade do ciclo de vida dos processos judiciais.

## 2. Padrões de Projeto e Práticas de GTI
*   **Processamento em Lote (Batch Ingestion):** A geração de faturas ocorre através de loops iterativos transacionais na camada de serviço, minimizando chamadas individuais ao banco de dados e mitigando anomalias de escrita (*dirty reads*).
*   **Consistência Transacional (ACID):** O fluxo de provisionamento é decorado com a anotação `@Transactional` do Spring. Caso ocorra qualquer falha matemática ou de infraestrutura durante o loop de parcelamento, um *rollback* total é acionado, impedindo a existência de contratos órfãos no banco de dados MySQL.
*   **Flexibilidade Temática:** A introdução do padrão baseado em Enums (`ModalidadeParcelamento`) remove regras de cálculo acopladas aos controllers, permitindo que o sistema calcule vencimentos dinamicamente de acordo com o andamento processual (Ex: parcelas atreladas a audiências ou sentenças).

## 3. Diagrama de Relacionamento de Entidades (DER)
[tb_contrato_honorario] 1 --------- * [tb_fatura]
|
| 1
*
[clientes]

## 4. Mecanismo de Triangulação de Pagamentos em Honorários de Sucumbência

A arquitetura da LexControl API resolve uma das maiores complexidades de domínio do direito financeiro: a cobrança de **Honorários de Sucumbência**. Como a sucumbência representa um título judicial onde a parte vencida (perdedor do processo) deve pagar diretamente ao advogado da parte vencedora, o sistema foi projetado para executar uma *Triangulação de Cobrança Estrita*.

## 5. Reutilização de Cadastros e LGPD

Diferente de gateways de pagamento terceiros tradicionais, a LexControl API prioriza a integridade dos dados e a segurança da informação ao reaproveitar o ecossistema de dados (`tb_usuario` / `clientes`) já autenticado e higienizado. O cruzamento nativo entre chaves estrangeiras garante que notificações automáticas de cobrança via WhatsApp, SMS ou E-mail acessem os metadados diretamente do perfil do cliente, eliminando redundâncias de redigitação em tela e mitigando falhas humanas de comunicação.

## 6. Automação de Juros e Multas em Cascata

A arquitetura de cálculo foi calibrada para mimetizar sistemas bancários homólogos. Ao persistir os parâmetros de penalidade de forma granular (juros diários fracionados ao mês e multas punitivas instantâneas), o motor de processamento assíncrono consegue recalcular em tempo de execução o valor atualizado para pagamento nas faturas em atraso, gerando links dinâmicos e protegendo o fluxo de caixa dos advogados contra perdas inflacionárias ou inadimplência prolongada.
