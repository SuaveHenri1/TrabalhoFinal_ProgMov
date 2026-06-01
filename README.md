# 🎮 Trabalho Final — ProgMov 2026

**Disciplina:** Programação para Dispositivos Mobile (ProgMov 2026)
**Instituição:** FACOM — UFMS
**Professora:** Ana Karina

## 👥 Alunos do Grupo

- [Henrique Alves]

---

> **Nota à Avaliação:** Este repositório documenta duas (2) propostas de protótipos de jogos para o trabalho final. O objetivo de ambas é utilizar mecânicas de simulação e gestão para aplicar na prática os conceitos exigidos na disciplina (Android Nativo, Base de Dados Local, Integração de Hardware, Listagens e UI/UX). Apenas uma será desenvolvida como projeto final, mediante validação.

---

## 🌾 OPÇÃO A — Projeto "Fazenda Medieval"

### Visão Geral

Protótipo offline de um jogo mobile de gestão de fazenda num cenário medieval. O foco principal é o ciclo básico de agricultura: o jogador gere as suas terras, planta sementes e aguarda o tempo de colheita para armazenar os recursos no inventário, utilizando recursos nativos do Android para simular a passagem de tempo e a gestão de itens.

### 👤 Papéis e Utilizadores

**Fazendeiro (Jogador Local):** Único utilizador do sistema. Pode registar-se, gerir o seu perfil, aceder às suas terras, plantar sementes, realizar colheitas, visualizar o inventário e verificar o estado geral (pontuação) da fazenda.

### ⚙️ Requisitos Funcionais e Técnicos

#### RF01 — A Guilda (Registo e Segurança)
O jogador regista-se tirando uma foto com a Câmara do dispositivo para o perfil. O sistema:
- Valida o e-mail e exige palavra-passe forte
- Guarda a **hash da palavra-passe** na base de dados
- Permite recuperação via envio de código (Intent para WhatsApp/SMS)
- Aplica testes de caixa preta para validação de campos

#### RF02 — Gestão da Lavoura (CRUD/Room)
O jogador interage com a terra. O sistema realiza operações na tabela `Lavouras` da base de dados local (SQLite/Room):
- **Inserção** — plantar
- **Leitura** — ver estado
- **Atualização** e **Eliminação** — colher

#### RF03 — Armazém e Inventário (Listagens)
- Consulta à base de dados para listar itens e trabalhadores do jogador
- Uso de `RecyclerView` e `CardView` para exibir imagens (armazenadas localmente) e dados dos itens

#### RF04 — Sistema de Tempo (Notificações e Alarmes)
- Crescimento das plantas gerido por agendamento via `AlarmManager`
- **Notificações Push** ao utilizador: _"A colheita está pronta!"_
- Acompanhado de **Áudio** (efeito sonoro de notificação do jogo)

#### RF05 — O Reino (Mapa e GPS) [EXTRA]
- Uso do serviço de **Localização/GPS** do dispositivo para determinar as coordenadas do jogador
- A fazenda é atribuída a um local físico no "Reino"

#### RF06 — O Mercadão (Requisições HTTP) [EXTRA]
- Consome um ficheiro `.json` externo (via GitHub ou API pública)
- Contém a tabela de preços do dia para calcular o valor do inventário

---

## 🤠 OPÇÃO B — Projeto "Acerto de Contas" *(The Reckoning Book)*

### Visão Geral

Jogo do género **Tycoon (Gerenciamento)** com elementos de **Roguelite** e **puzzle matemático**, em perspetiva de primeira pessoa. O jogador é um ex-pistoleiro que gere um bar no faroeste. Terá de preparar bebidas interativamente e, ao meio-dia, usar o raciocínio lógico-matemático para vencer duelos — misturando gestão de inventário e educação matemática.

### 👤 Papéis e Utilizadores

**Bartender/Pistoleiro (Jogador Local):** Único utilizador. Pode registar a sua licença de estabelecimento, gerir o stock do bar, preparar bebidas para clientes e participar no minijogo de duelos matemáticos diários.

### ⚙️ Requisitos Funcionais e Técnicos

#### RF01 — Licença de Bartender (Login e Segurança)
Ecrã inicial simulando a emissão de uma licença do faroeste. O jogador:
- Tira a sua **foto** (Câmara)
- Cria a conta com validação de dados, palavra-passe forte com hash e recuperação de acesso
- Sistema protegido contra acessos não autorizados

#### RF02 — Preparação de Bebidas (Sensores e Áudio)
Para preparar a bebida de um cliente, o jogador seleciona os ingredientes e deve **chacoalhar o telemóvel fisicamente**. O sistema:
- Capta o movimento através do **Acelerómetro** (Sensor Nativo)
- Reproduz **Áudio** (som de vidro e líquido) para confirmar o sucesso da ação

#### RF03 — O Duelo Matemático (Algoritmo e UI)
Ao "meio-dia", o jogo pausa para o duelo:
- É gerada uma **equação matemática procedural** (ex: $X + 2 = 5$)
- O jogador tem um **limite de tempo**
- Vitória ou derrota é registada

#### RF04 — Gestão do Bar (CRUD e Room)
Toda a operação do bar é persistida na base de dados local:
- `Tabela_Estoque` — ingredientes e bebidas disponíveis
- `Tabela_Progresso` — histórico de duelos e progresso
- Operações: **Inserir** (comprar mantimentos), **Ler** (ver stock), **Atualizar/Eliminar** (consumir ao fazer bebidas)

#### RF05 — O Livro de Contas (Listagens)
Uso de `RecyclerView` e `CardView` para mostrar:
- Inventário atual do bar
- Lista de inimigos derrotados
- Menu de funcionários a contratar

Imagens guardadas localmente no dispositivo.

#### RF06 — Eventos Diários (Notificações e HTTP) [EXTRA]
- **Notificação diária:** _"O forasteiro chegou para o duelo!"_
- **Requisição HTTP** a uma API de cotação de moedas para simular o valor do "Ouro" e alterar o custo dos fornecedores em tempo real

---

## ✅ Critérios de Qualidade *(Aplicados a ambas as opções)*

### Usabilidade e Acessibilidade
- Interface adaptada com botões grandes (touch targets adequados)
- Suporte a **alto contraste**
- Feedback **tátil e sonoro** para tolerância ao erro

### Testes de Caixa Preta
- Tratamento rigoroso de e-mails inválidos, palavras-passe curtas e campos em branco
- Prevenção de **SQL Injection**
- Bloqueio de acessos diretos sem sessão (login) válida
