# ⚖️ LexControl Ecosystem

> Sistema de alta performance para gestão jurídica e controle financeiro.

O **LexControl** é uma plataforma robusta desenvolvida para eliminar gargalos operacionais em escritórios de advocacia. O ecossistema integra um backend resiliente em Java com um frontend moderno e responsivo em Next.js, utilizando as tecnologias mais recentes do mercado para garantir escalabilidade e segurança.

---

### 📸 Preview do Sistema

#### 💻 Desktop Experience

A interface desktop foi projetada para alta densidade de informação e produtividade constante.

| Fluxo de Autenticação | Dashboard Principal | Lista de Clientes |
| --- | --- | --- |
|  |  |  |

#### 📱 Mobile-First Design

O sistema garante que o advogado tenha acesso aos dados em tempo real, com uma interface otimizada para dispositivos móveis.

<div align="center">
<video src="docs/gifs/mobile-ficha-cliente.webm" width="32%" autoplay loop muted></video>
<img src="docs/screenshots/mobile-home.png" width="32%" alt="Home Mobile">
<img src="docs/screenshots/mobile-menu.png" width="32%" alt="Menu Mobile">
</div>

---

### 🚀 Stack Tecnológica

#### **Backend (LexControl API)**

* **Core:** Java 25 & Spring Boot 4.0.
* **Persistência:** MySQL 8.0 com controle de versionamento via **Flyway Migrations**.
* **Segurança:** Spring Security (Preparado para implementação de JWT).
* **Arquitetura:** Padrão RESTful com isolamento rigoroso via DTOs e Services.

#### **Frontend (LexControl Web)**

* **Framework:** Next.js 15 (App Router) & React 19.
* **Estilização:** Tailwind CSS com metodologia de design atômico.
* **Gerenciamento de Estado:** TanStack Query (React Query) & Context API.
* **Ícones:** Lucide React.

---

### ⚙️ Configuração de Ambiente (DevOps)

O projeto utiliza o recurso de **Git Worktrees**, permitindo o desenvolvimento simultâneo em múltiplas branches (`master`, `BackEnd`, `FrontEnd`) em pastas separadas, sem a necessidade de múltiplos clones.

#### **1. Setup Automático**

Certifique-se de ter o Python instalado e execute o script de automação na raiz do seu diretório de trabalho:

```bash
python setup_lexcontrol.py

```

*Este script instanciará as pastas `lexcontrol-main`, `lexcontrol-back` e `lexcontrol-front` sincronizadas entre si.*

#### **2. Execução dos Módulos**

* **Frontend:**
```bash
cd lexcontrol-front
npm install
npm run dev

```


* **Backend:**
* Importe a pasta `lexcontrol-back` no IntelliJ IDEA.
* Certifique-se de que o **JDK 25** está configurado.
* Execute a aplicação via Spring Boot Dashboard. A API estará disponível em `localhost:8080`.



---

### 🏗️ Arquitetura e Engenharia de Software

O desenvolvimento segue princípios rigorosos de **POO** e **Clean Code**:

* **Arquitetura Modular:** Localizada em `src/modules/`, separando as lógicas de negócio por domínio (ex: Clientes, Notas, Financeiro).
* **Single Source of Truth:** Tipagens globais em `src/shared/types/` para garantir consistência em todo o ecossistema.
* **Blindagem de Dados:** Camada de serviço no frontend responsável por tratar valores nulos, erros de API e garantir a integridade dos dados antes da renderização.
* **Componentes Reutilizáveis:** Átomos de interface localizados em `src/shared/components/` para garantir uniformidade visual e facilidade de manutenção.

---

### 👨‍💻 Governança de Código

Para manter a integridade do ambiente de produção:

1. **Feature Branches:** Todo desenvolvimento deve ocorrer em branches temporárias.
2. **Code Review:** Commits diretos na branch `master` são estritamente proibidos.
3. **Padronização de Imagens:** Novos screenshots devem seguir o padrão `plataforma-funcionalidade.png` e serem armazenados em `docs/screenshots/`.

---

**LexControl** - *Transformando a gestão jurídica através da tecnologia.*
