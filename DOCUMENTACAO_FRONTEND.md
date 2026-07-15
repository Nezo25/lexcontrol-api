# 📘 Documentação Oficial: Front-End LexControl

Este documento serve como o guia definitivo para a equipe de Front-End que irá construir a interface do **LexControl**. O back-end já foi construído de forma robusta utilizando Spring Boot, com foco em escalabilidade, segurança e integração bancária.

Nosso objetivo é que o Front-End seja construído com o mesmo nível de excelência, preferencialmente utilizando frameworks modernos como **Next.js / React** e **TailwindCSS** para uma interface fluida (Single Page Application).

---

## 🔐 1. Autenticação e Segurança (JWT)

A API é protegida por **Spring Security** e utiliza autenticação baseada em tokens JWT.

- **Login**: O usuário deve enviar `email` e `senha` para `/auth/login`.
- **Token**: A API retornará um `accessToken` e um `refreshToken`.
- **Interceptor (Axios/Fetch)**: O Front-End **deve** configurar um interceptor HTTP global para injetar o header `Authorization: Bearer <accessToken>` em todas as requisições protegidas.
- **Refresh Flow**: Quando a API retornar `401 Unauthorized` (Token expirado), o Front-End deve silenciosamente enviar o `refreshToken` para a rota `/auth/refresh` para obter um novo `accessToken` e refazer a requisição original sem deslogar o usuário.

---

## 🚨 2. Padrão de Respostas e Tratamento de Erros

O Back-End possui um **Global Exception Handler**. O Front-End não precisa tentar adivinhar mensagens de erro. Se algo der errado, a API sempre retornará um JSON padronizado com os detalhes.

### Códigos HTTP Utilizados:
- `200 / 201`: Sucesso.
- `400 Bad Request`: Erro de validação de formulário ou regra de negócio violada.
- `404 Not Found`: Recurso não encontrado (ex: acessar um cliente deletado).
- `500 Internal Server Error`: Falha crítica do servidor.

### Exemplo de Erro de Validação (400):
Se o Front-End enviar um CPF inválido ou em branco, a resposta será:
```json
{
  "status": 400,
  "message": "Erro de validação nos campos",
  "timestamp": 1705064200000,
  "erros": [
    "cpf: CPF inválido",
    "nomeCliente: O nome não pode estar em branco"
  ]
}
```
**Missão do Front-End**: Mapear a lista de `erros` e destacar os campos de input de vermelho na tela (ex: mostrar os alertas embaixo de cada input).

---

## 👥 3. Módulos e CRUDs

A API expõe rotas RESTful padronizadas (`GET`, `POST`, `PUT`, `DELETE`).

### Clientes (`/api/v1/clientes`)
- É a base do sistema. Possui dados como Nome, CPF, RG, Endereço.
- **Regra Importante**: A API recusa a criação de dois clientes com o mesmo CPF. O Front-End deve exibir um "Toast" ou "Alerta" avisando o usuário caso receba esse erro.
- A exclusão de um cliente é um Soft Delete (ou Hard Delete dependendo das amarrações). O Front-End deve exibir pop-ups de confirmação *"Tem certeza que deseja excluir?"*.

### Advogados e Escritórios (`/api/v1/advogados` e `/api/v1/escritorios`)
- Um advogado pode estar vinculado a um escritório.
- Os formulários devem possuir inputs formatados adequadamente (Máscara de CPF, Máscara de OAB e Máscara de CNPJ para os escritórios).

---

## 💸 4. Módulo Financeiro: A Integração Asaas (O Coração do SaaS)

Este é o módulo mais complexo do LexControl. O back-end conversa nativamente com o gateway de pagamento Asaas de forma autônoma.

### Como funciona o Fluxo para o Front-End:
1. **Emissão**: Quando o usuário gera um Contrato/Cobrança no Front-End, o Back-End cria o cliente no Asaas, emite a Fatura lá e gera um link de pagamento (`invoiceUrl`).
2. **Processamento Assíncrono**: Para evitar travamentos, a API envia a cobrança para o Asaas "por debaixo dos panos".
3. **Exibição na Tela**: O Front-End deve exibir uma lista ou painel (Dashboard) com todas as faturas do cliente.

### Status da Fatura (`StatusFatura`)
O Front-End deve criar "Badges" visuais para cada status que a Fatura pode ter no Banco de Dados:
- 🟡 `PENDENTE`: Aguardando o cliente pagar. (Front-End deve exibir o botão **"Copiar Link de Pagamento"** acessando o campo `linkPagamentoUrl`).
- 🟢 `PAGA`: Cliente pagou. (Ocultar botão de pagamento).
- 🔴 `ATRASADA`: Passou da data de vencimento.
- ⚪ `CANCELADA`: Cobrança estornada ou cancelada.
- 🟠 `ERRO_EMISSAO`: O Asaas negou a criação da fatura (ex: erro de rede ou CPF inválido). O Front-End deve exibir um botão **"Tentar Emitir Novamente"**.

### A Mágica do Webhook
O Front-End **NÃO** precisa mandar requisições para checar se o boleto foi pago. O próprio Asaas avisa o nosso Back-End em tempo real (Webhook).
- Se a fatura estava 🟡 `PENDENTE` e o cliente final fez o Pix, em questão de segundos o banco de dados muda sozinho para 🟢 `PAGA`.
- **Dica para o Front-End**: Se o usuário estiver na tela da fatura aguardando o pagamento, é interessante ter um botão de "Atualizar Status" ou um polling (Requisição a cada 10s) para ver se o status já mudou para `PAGA` e exibir um confete na tela.

---

## 🎨 5. Expectativas de UI / UX

O LexControl é um software premium para área jurídica.
- **Design Moderno**: Evitar telas brancas com tabelas cruas. Usem sombras, bordas arredondadas (Border Radius) e micro-interações.
- **Feedback Constante**: Se um request for para o Back-End, botões devem exibir um estado de "Loading..." (Spinner) e desativar os cliques para impedir requisições duplicadas.
- **Mobile-First**: A interface deve ser plenamente utilizável em celulares, pois advogados viajam bastante e checam faturas pelo celular.
- **Tabelas com Paginação e Filtros**: As listagens de clientes e honorários precisam ter barras de pesquisa e filtros por status de pagamento.

## ⚖️ 6. Casos Especiais: Sucumbência e Êxito

Do ponto de vista de negócios e UI, um contrato de **Sucumbência** ou **Êxito** não é uma cobrança imediata, é uma **expectativa de direito**. O advogado só recebe se ganhar a causa.

### Como o Front-End deve tratar isso:
1. **Na tela de "Novo Contrato"**: Se o usuário selecionar o Tipo de Honorário `SUCUMBENCIA` ou `EXITO`, o Front-End deve esconder os campos de "Quantidade de Parcelas" e "Vencimento Inicial", e exibir campos para coletar os dados da **Parte Perdedora** (Nome e Documento). O Back-End salvará isso com o status `AGUARDANDO_GANHO_CAUSA` (Nenhum boleto será gerado no Asaas neste momento).
2. **Na tela de Detalhes do Contrato**: Para contratos nesse status, exibir um botão brilhante de ação primária: **"🏆 Declarar Ganho de Causa"**.
3. **Modal de Ganho de Causa**: Ao clicar no botão, abrir um Modal perguntando:
   - Qual foi o Valor Final Arbitrado pelo Juiz?
   - Em quantas parcelas a parte perdedora vai pagar?
   - Qual a data do primeiro vencimento?
4. **Requisição Final**: Enviar esses dados via `POST /api/v1/cobrancas/contratos/{contratoId}/vencer-causa`. Nesse momento, o Back-End assumirá o controle, criará o perdedor no Asaas e disparará todas as faturas oficiais!

---
**Fim da Documentação**. Em caso de dúvidas sobre contratos da API, consultem a documentação do Swagger que está rodando em `http://localhost:8080/swagger-ui.html`.
