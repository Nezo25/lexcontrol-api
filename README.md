<div align="center">
  <h1>⚖️ LexControl Ecosystem</h1>
  <p><strong>Engenharia de Software de Alta Performance para Gestão Jurídica</strong></p>
  <p>
    <img src="https://img.shields.io/badge/Java_25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 25" />
    <img src="https://img.shields.io/badge/Next.js_15-000000?style=for-the-badge&logo=next.js&logoColor=white" alt="Next.js" />
    <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring" />
    <img src="https://img.shields.io/badge/Tailwind_CSS-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white" alt="Tailwind" />
  </p>
  <p><em>O LexControl é um ecossistema completo para escritórios de advocacia que buscam automação financeira e controle processual. Desenvolvido com arquitetura modular, seguindo rigorosamente os princípios de Clean Code e SOLID.</em></p>
</div>

---

## 💻 Experiência Desktop (Gestão e Autenticação)

<div align="center">
  <a href="https://github.com/Nezo25/lexcontrol-api/blob/master/docs/gifs/gif_final.mp4" target="_blank">
    <img src="https://img.shields.io/badge/▶_Assistir_Vídeo_de_Autenticação-4285F4?style=for-the-badge" alt="Ver Login" />
  </a>
</div>
<br/>

<table align="center" width="100%">
  <tr>
    <td align="center" width="50%"><b>Dashboard Principal</b></td>
    <td align="center" width="50%"><b>Lista de Clientes</b></td>
  </tr>
  <tr>
    <td><img src="docs/screenshots/HomeDesktop.png" width="100%" alt="Dashboard Desktop"></td>
    <td><img src="docs/screenshots/ClienteDesk.png" width="100%" alt="Lista de Clientes Desktop"></td>
  </tr>
  <tr>
    <td align="center" colspan="2"><b>Ficha Detalhada do Cliente (Desktop)</b></td>
  </tr>
  <tr>
    <td align="center" colspan="2"><img src="docs/screenshots/FichaDoClienteDesk.png" width="80%" alt="Ficha Desktop"></td>
  </tr>
</table>

---

## 📱 Mobile-First Design (Operação em Campo)

<div align="center">
  <a href="https://github.com/Nezo25/lexcontrol-api/blob/master/docs/gifs/projeto_celular.mp4" target="_blank">
    <img src="https://img.shields.io/badge/▶_Vídeo:_Welcome_Robot_🤖-34A853?style=for-the-badge" alt="Ver Robo" />
  </a>
  &nbsp;&nbsp;
  <a href="https://github.com/Nezo25/lexcontrol-api/blob/master/docs/gifs/FichaDoClienteMobile.webm" target="_blank">
    <img src="https://img.shields.io/badge/▶_Vídeo:_Ficha_do_Cliente-FBBC05?style=for-the-badge" alt="Ver Ficha" />
  </a>
</div>
<br/>

### 1. Visão Geral (Home, Menu e Lista)
<table align="center" width="100%">
  <tr>
    <td align="center" width="33%"><img src="docs/screenshots/HomeMobile.png" width="100%" alt="Home"></td>
    <td align="center" width="33%"><img src="docs/screenshots/MenuLateralMobile.png" width="100%" alt="Menu"></td>
    <td align="center" width="33%"><img src="docs/screenshots/ClienteMobile.png" width="100%" alt="Lista"></td>
  </tr>
</table>

### 2. Interações e Alertas (Detalhes)
<table align="center" width="100%">
  <tr>
    <td align="center" width="33%"><b>Assistente Virtual</b></td>
    <td align="center" width="33%"><b>Ficha do Cliente</b></td>
    <td align="center" width="33%"><b>Notificações</b></td>
  </tr>
  <tr>
    <td align="center"><img src="docs/screenshots/Robozinho.png" width="80%" alt="Robozinho Welcome"></td>
    <td align="center"><img src="docs/screenshots/FichaDoClienteMobile.png" width="80%" alt="Ficha Mobile"></td>
    <td align="center"><img src="docs/screenshots/NotificaoMobile.png" width="80%" alt="Notificacao Mobile"></td>
  </tr>
</table>

---

## 🚀 Stack Tecnológica de Elite

* **Backend:** Java 25, Spring Boot 4.0, MySQL 8.0, Flyway Migrations, Spring Security.
* **Frontend:** Next.js 15 (App Router), React 19, Tailwind CSS, TanStack Query.

---

## ⚙️ Como Executar (Instalação e Setup Local)

O projeto utiliza **Git Worktrees** para gerenciar múltiplos contextos em pastas separadas, evitando conflitos de arquivos e agilizando o desenvolvimento.

### Passo 1: Automação DevOps (Script Python)
1. Coloque o arquivo `setup_lexcontrol.py` na pasta raiz do seu diretório de projetos.
2. No terminal, execute o comando abaixo para instanciar as pastas separadas (Main, Front e Back):
```bash
python setup_lexcontrol.py
