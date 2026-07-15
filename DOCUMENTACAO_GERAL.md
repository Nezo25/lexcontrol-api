# Documentação Geral - LexControl API

Este documento consolida toda a arquitetura e as regras de negócio implementadas no backend do LexControl, abrangendo os domínios de Autenticação, Cadastros Base, Advocacia e Faturamento.

## 1. Arquitetura e Tecnologias
- **Framework**: Spring Boot 3
- **Linguagem**: Java
- **Banco de Dados**: MySQL (porta 3310 mapeada para o SGBD)
- **Migrations**: Flyway (Versões V1 a V10)
- **ORM**: Spring Data JPA / Hibernate
- **Segurança**: Spring Security + JWT
- **Build/Gerenciamento**: Maven

---

## 2. Domínios e Entidades

### 2.1. Usuário e Autenticação (`/usuarios` e `/auth`)
- **Tabela**: `tb_usuario`
- **Regras**: 
  - Todo usuário possui uma _Role_ de acesso (`ADMIN` ou `USER`).
  - As senhas são encriptadas com BCrypt antes da persistência.
  - A rota de login retorna um Token JWT que deve ser inserido no Header (`Authorization: Bearer <token>`) para acessar as demais rotas da API.

### 2.2. Cliente (`/clientes`)
- **Tabela**: `clientes`
- **Regras**:
  - Cadastro exclusivo dos dados demográficos da pessoa física ou jurídica (`nomeCliente`, `cpf`, `rg`, `telefone`, `endereco`).
  - Um cliente **não** guarda dados de contratos ou de valores a receber, já que o mesmo cliente pode firmar dezenas de contratos paralelos e independentes no escritório.

### 2.3. Escritório (`/escritorios`)
- **Tabela**: `escritorios`
- **Regras**:
  - Representa a entidade corporativa (PJ) do escritório de advocacia (`razaoSocial`, `cnpj`, `nomeEscritorio`, `endereco`).
  - Serve como base agrupadora para os advogados vinculados.

### 2.4. Advogado (`/advogados`)
- **Tabela**: `advogados`
- **Regras**:
  - Profissional vinculado obrigatoriamente a um **Escritório** (`ManyToOne`).
  - Relacionado aos **Clientes** através de uma tabela intermediária `cliente_advogado` (`ManyToMany`), permitindo que um advogado tenha vários clientes e um cliente seja atendido por uma equipe de advogados.
  - Possui `Especialidade` mapeada por um Enum (CRIMINAL, CIVIL, TRABALHISTA, etc).

### 2.5. Motor de Faturamento (`/cobrancas`)
- **Tabelas**: 
  - `tb_contrato_honorario` (Pai - Regras do acordo comercial)
  - `tb_fatura` (Filha - Boletos / Parcelas geradas)
  - `tb_contrato_canais_notificacao` (Preferências de envio)
- **Regras de Negócio**:
  - **Obrigatoriedade**: Todo contrato gerado deve referenciar o ID de um `Cliente` existente. A amarração ocorre no endpoint (Ex: `POST /cobrancas/emitir/{clienteId}`).
  - **Tipos de Honorários**:
    - `CONTRATUAL`: A cobrança é direcionada ao próprio cliente cadastrado.
    - `SUCUMBENCIA`: A cobrança sofre uma *triangulação*. O contrato fica armazenado sob o ID do cliente, porém os boletos e notificações são disparados em nome da parte perdedora (`nome_pagador_sucumbencia`).
  - **Modalidades de Parcelamento**:
    - `AVISTA`: O motor gera 1 parcela (fatura) no ato.
    - `PARCELADO_MENSUAL`: O motor fatia o valor em _N_ parcelas, projeta as datas somando meses consecutivos e salva na base de dados de forma automática via JPA Cascading.
  - **Frequência e Penalidades**: Permite configurar juros e multas percentuais, além da periodicidade do faturamento (Mensal, Bimestral, Anual).

---

## 3. Fluxo das Migrations (Flyway)
O banco foi construído de forma evolutiva e segura. As migrations aplicadas garantem a integridade da modelagem.
- **V1**: Criação inicial da base (Clientes).
- **V2**: Criação de Advogados e Escritórios.
- **V3**: Relação Advogado <-> Cliente.
- **V4 a V6**: Ajustes em status e regras de negócio base.
- **V7 a V9**: Implementação bruta do Motor de Faturamento (Tabelas e Constraints).
- **V10**: Refatoração (Limpeza). Drop dos campos financeiros antigos da tabela de clientes, migrando a arquitetura 100% para a _Fase 2_ de faturamento.
