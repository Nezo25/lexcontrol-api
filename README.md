# ⚖️ LexControl Ecosystem

> **Engenharia de Software de Alta Performance para Gestão Jurídica**

O **LexControl** é um ecossistema completo para escritórios de advocacia que buscam automação financeira e controle processual. Sincronia absoluta entre **Java 25** e **Next.js 15**.

---

## 💻 Experiência Desktop (Gestão e Autenticação)

### 1. Fluxo de Login
*(Veja o vídeo abaixo renderizado pelo GitHub)*

![Fluxo de Login Desktop](docs/gifs/gif_final.mp4)

### 2. Dashboard Principal e Gestão de Clientes

<div align="center">
  <img src="docs/screenshots/HomeDesktop.png" width="49%" alt="Dashboard Desktop">
  <img src="docs/screenshots/ClienteDesk.png" width="49%" alt="Lista de Clientes Desktop">
</div>

---

## 📱 Mobile-First Design (Operação em Campo)

### 1. Welcome Robot 🤖 (Assistente de Boas-Vindas)

![Welcome Robot](docs/gifs/projeto_celular.mp4)

### 2. Ficha Completa do Cliente

![Ficha do Cliente](docs/gifs/FichaDoClienteMobile.webm)

### 3. Visão Geral Mobile (Home, Menu e Lista)

<div align="center">
  <img src="docs/screenshots/HomeMobile.png" width="32%" alt="Home Mobile"> 
  <img src="docs/screenshots/MenuLateralMobile.png" width="32%" alt="Menu Lateral Mobile"> 
  <img src="docs/screenshots/ClienteMobile.png" width="32%" alt="Lista de Clientes Mobile">
</div>

---

## 🪄 Automação DevOps (Script Python)

Criamos um **Script em Python** que prepara todo o ambiente do LexControl automaticamente usando a arquitetura de `Git Worktrees`.

Ele clona a raiz (`main`) e cria instâncias físicas separadas para o `front` e o `back`, permitindo trabalhar simultaneamente sem conflito de branches.

**Como invocar o ambiente:**
1. Tenha o Python instalado.
2. Coloque o arquivo `setup_lexcontrol.py` na sua pasta de projetos.
3. No terminal, execute:
```bash
python setup_lexcontrol.py
