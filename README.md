# 📱 Central de Aplicativos (AppHub)

Um aplicativo Android que reúne múltiplas funcionalidades úteis em uma interface centralizada. Desenvolvido em Kotlin com foco na praticidade e facilidade de uso.

## 🎯 Sobre o Projeto

A Central de Aplicativos é um hub que concentra três aplicações distintas:
- **Placar de Basquete** - Sistema completo de pontuação para jogos
- **Calculadora Científica** - Calculadora avançada com histórico
- **To-Do List** - Gerenciador de tarefas com prioridades e datas

## ⚡ Funcionalidades Principais

### 🏀 Placar de Basquete
- **Pontuação Completa**: Suporte para cestas de 1, 2 e 3 pontos
- **Estatísticas em Tempo Real**: Acompanhamento de cestas e faltas por time
- **Sistema de Liderança**: Destaque visual para o time que está vencendo (👑)
- **Log de Eventos**: Histórico completo de todas as ações da partida
- **Controles de Partida**: Opções para reiniciar ou finalizar o jogo
- **Resumo Final**: Dialog com estatísticas completas ao fim da partida

### 🧮 Calculadora Científica
- **Operações Básicas**: Adição, subtração, multiplicação e divisão
- **Funções Avançadas**: 
  - Porcentagem
  - Inversão de sinal (+/-)
  - Backspace para correções
- **Histórico Inteligente**: Armazena as últimas 5 operações realizadas
- **Interface Intuitiva**: Display principal com preview da operação
- **Tratamento de Erros**: Proteção contra divisão por zero
- **Persistência de Estado**: Mantém dados durante rotação da tela

### 📝 To-Do List
- **Criação de Tarefas**: Interface simples para adicionar novas tarefas
- **Sistema de Prioridades**: Classificação por importância
- **Seletor de Data**: DatePicker integrado para definir prazos
- **Lista Organizada**: RecyclerView para exibição otimizada das tarefas
- **Gerenciamento de Status**: Controle de tarefas concluídas/pendentes

## 🛠️ Tecnologias Utilizadas

- **Linguagem**: Kotlin + Java
- **IDE**: Android Studio
- **SDK Mínimo**: Android API (verificar build.gradle)
- **Arquitetura**: Activities com navegação hierárquica
- **UI Components**: 
  - RecyclerView para listas
  - Material Design Components
  - DatePickerDialog
  - AlertDialog
  - Toast para feedback

## 📦 Dependências Principais

```gradle
androidx-core-ktx = "1.17.0"
androidx-appcompat = "1.7.1" 
material = "1.13.0"
androidx-activity = "1.11.0"
androidx-constraintlayout = "2.2.1"
```

## 🚀 Como Executar

### Requisitos
- Android Studio Arctic Fox ou superior
- JDK 8 ou superior
- Android SDK
- Dispositivo Android ou Emulador (API 21+)

### Passos para Instalação

1. **Clone o Repositório**
   ```bash
   git clone https://github.com/Matheussfreitas/hub_app.git
   cd hub_app
   ```

2. **Abra no Android Studio**
   - File → Open → Selecione a pasta do projeto

3. **Sincronize as Dependências**
   - Aguarde o Gradle sincronizar automaticamente
   - Ou execute: `./gradlew build`

4. **Execute o Aplicativo**
   - Conecte um dispositivo Android ou inicie um emulador
   - Clique em "Run" ou use `Shift + F10`

## 📱 Como Usar

### Tela Principal
A tela inicial apresenta três botões principais para acessar cada funcionalidade:

- **🏀 Placar de Basquete**: Gerenciar jogos de basquete
- **🧮 Calculadora**: Realizar cálculos matemáticos  
- **📝 To-Do**: Organizar suas tarefas

### Navegação
- Cada aplicativo abre em sua própria Activity
- Use o botão "Voltar" ou a seta na ActionBar para retornar ao menu principal
- A navegação é hierárquica e intuitiva

## 🏗️ Estrutura do Projeto

```
app/src/main/java/com/gabrielmatheus/apphub/
├── MainActivity.kt              # Tela principal do hub
├── CalcActivity.kt             # Calculadora científica
├── PlacarActivity.kt           # Placar de basquete
└── todo/
    ├── TodoActivity.kt         # Gerenciador de tarefas
    ├── Task.java              # Modelo de dados da tarefa
    └── TodoAdapter.java       # Adapter para RecyclerView
```

## ✨ Destaques Técnicos

- **Código Híbrido**: Combinação inteligente de Kotlin e Java
- **UI Responsiva**: Layouts que se adaptam a diferentes tamanhos de tela
- **Gerenciamento de Estado**: Preservação de dados durante mudanças de configuração
- **Tratamento de Erros**: Validações e feedback adequado ao usuário
- **Padrões Android**: Seguindo as melhores práticas de desenvolvimento

## 🎨 Interface do Usuário

- **Material Design**: Visual moderno e consistente
- **Navegação Intuitiva**: ActionBar com navegação hierárquica
- **Feedback Visual**: Toasts, dialogs e animações
- **Cores e Tipografia**: Esquema visual harmonioso
- **Acessibilidade**: Componentes otimizados para diferentes usuários

# 🛠️ Guia de Debug e Rastreamento de Logs (App Hub)

Este documento detalha os roteiros de debug e as análises de funcionamento para os aplicativos presentes no App Hub, utilizando a classe centralizada `LogHelper` (`TAG: AppHub`).

**Filtro de Logcat Recomendado:** `TAG:AppHub` ou `Package:com.gabrielmatheus.apphub` com o nível de log **Debug (D)**.

---

## 🔍 Roteiro de Debug 1: Calculadora (CalcActivity)

A **Calculadora (App 2)** exige rastreamento detalhado de estado, especialmente durante operações em cadeia e tratamento de exceções (como divisão por zero).

### Cenário A: Cadeia de Operações (Chaining Operation)

**Objetivo:** Verificar se a calculadora mantém o estado e calcula corretamente operações contínuas sem o uso do `=` (Ex: `10 + 5 * 2`).

| Passo | Ação do Usuário/Sistema | Funcionamento Esperado | Análise de Mal Funcionamento (Bug) | Nível de Log a Acompanhar |
| :--- | :--- | :--- | :--- | :--- |
| **1** | Digita `10`. Pressiona `+`. Digita `5`. | `operand=10.0`, `pendingOp=+`. Display: `10 + 5`. | `currentInput` não é resetado, e o display mostra `105`. | **D**ebug: `onOperator` |
| **2** | Pressiona `*` (sem antes pressionar `=`). | O cálculo `10 + 5 = 15` ocorre **internamente**. `operand` se torna `15.0`. `pendingOp` se torna `*`. | O cálculo intermediário não ocorre, e o App tenta calcular `10 * 5` depois. | **D**ebug: `onEquals` (cálculo), `onOperator` (estado) |
| **3** | Digita `2` e pressiona `=`. | O cálculo `15 * 2 = 30` é finalizado. Display mostra `30`. | O resultado final é incorreto (ex: `17` ou `20`). | **D**ebug: `onEquals` |

### Cenário B: Divisão por Zero e Persistência

**Objetivo:** Testar o tratamento de erros e a recuperação do estado da aplicação (`onSaveInstanceState`/`onRestoreInstanceState`).

| Passo | Ação do Usuário/Sistema | Funcionamento Esperado | Análise de Mal Funcionamento (Bug) | Nível de Log a Acompanhar |
| :--- | :--- | :--- | :--- | :--- |
| **1** | Digita `80`. Pressiona `/`. Digita `0`. Pressiona `=`. | Exibe `Toast` de "Divisão por zero". O log **ERROR** é gerado. | O App **crash** ou o resultado é exibido como `Infinity`. | **E**rror: `performOperation` |
| **2** | Digita `+` (após a falha). | O valor `80` é mantido como o `operand` para a próxima operação. | A calculadora trava ou o `operand` é resetado para `0` ou `null`. | **D**ebug: `onOperator` |
| **3** | Gira o dispositivo (mudança de configuração). | Os valores `operand=80.0` e `pendingOp='+'` são restaurados. | O display reseta para `0` ou a operação pendente é perdida. | **I**nfo: `onSaveInstanceState` / `onRestoreInstanceState` |


## 🛒 Roteiro de Debug 2: Todo App (TodoActivity)

O **Todo App (App 3)** foca na persistência de dados (`RecyclerView` e `onSaveInstanceState`) e manipulação de input.

### Cenário A: Falha na Persistência de Estado (BUG CRÍTICO)

**Objetivo:** Testar a recuperação dos dados após uma interrupção (ex: rotação de tela).
*(Nota: O código revela que o `TodoAdapter.addTask` não atualiza a lista `tasks` da Activity, o que causa perda de dados.)*

| Passo | Ação do Usuário/Sistema | Funcionamento Esperado | Análise de Mal Funcionamento (Bug) | Nível de Log a Acompanhar |
| :--- | :--- | :--- | :--- | :--- |
| **1** | Adiciona 2 tarefas ("Comprar Pão", "Reunião"). | Ambas as listas (Activity e Adapter) têm 2 itens. | A lista `tasks` da **Activity** está **vazia**, pois o adapter não a atualizou. | **D**ebug: `buttonAddTask` |
| **2** | Gira o dispositivo 🔄. | `onSaveInstanceState` salva as 2 tarefas. | O log **D**ebug em `onSaveInstanceState` mostra `Salvando 0 tarefas.`. **PERDA DE DADOS**. | **D**ebug: `onSaveInstanceState` |
| **3** | O dispositivo conclui a rotação. | As tarefas são restauradas e visíveis. | O log **I**nfo em `onRestoreInstanceState` mostra `0 tarefas restauradas com sucesso.`. O `RecyclerView` está vazio. | **I**nfo/ **E**rror: `onRestoreInstanceState` |

### Cenário B: Validação e Seleção de Data

**Objetivo:** Assegurar que os inputs obrigatórios e as seleções de data sejam tratados corretamente.

| Passo | Ação do Usuário/Sistema | Funcionamento Esperado | Análise de Mal Funcionamento (Bug) | Nível de Log a Acompanhar |
| :--- | :--- | :--- | :--- | :--- |
| **1** | Clica em **Adicionar Tarefa** com o campo **Título vazio**. | O log **WARNING** é gerado, e a tarefa não é criada (`return`). | O App tenta criar uma tarefa com título vazio e *crash*. | **W**arning: `buttonAddTask` |
| **2** | Clica em **Selecionar Data**. Seleciona `01/10/2025`. | O `textViewDate` é atualizado para a data correta. O log **V**erbose é gerado com a data exata. | O `textViewDate` mostra a data errada (ex: `01/9/2025` - erro de mês). | **V**erbose: `pickDate` |
| **3** | Adiciona Tarefa **"Reunião"** (com a data selecionada). | A tarefa é criada e o log **D**ebug confirma a data `01/10/2025` na tarefa. | A tarefa é adicionada com a data `Sem data` (falha na captura da variável `selectedDate`). | **D**ebug: `buttonAddTask` |

---

## 🏀 Roteiro de Debug 3: Placar de Basquete (PlacarActivity)

A **PlacarActivity (App 1)** testa a integridade dos contadores de estado e a lógica de UI dinâmica (destaque do líder).

### Cenário A: Integridade da Pontuação e Faltas

**Objetivo:** Garantir que o placar, cestas e faltas sejam incrementados e rastreados de forma independente.

| Passo | Ação do Usuário/Sistema | Funcionamento Esperado | Análise de Mal Funcionamento (Bug) | Nível de Log a Acompanhar |
| :--- | :--- | :--- | :--- | :--- |
| **1** | Clica 2x no **3 Pontos** Time A. Clica 1x no **1 Ponto** Time A. | Placar A: `7`. Cestas A: `3`. Os logs **D**ebug rastreiam o estado após cada clique. | A variável `cestasTimeA` permanece em `2` (tiro livre não contado como cesta). | **D**ebug: `adicionarPontos` |
| **2** | Clica 1x no botão **Falta** do Time B. | Placar A permanece em `7`. `faltasTimeB = 1`. | A pontuação do Time B é inadvertidamente incrementada junto com a falta. | **D**ebug: `adicionarFalta` |
| **3** | Clica no botão **Reiniciar Partida**. | Todas as variáveis (`pontuacao`, `cestas`, `faltas`) retornam a **0**. O log **I**nfo confirma o reset. | O placar na UI volta a 0, mas as estatísticas (cestas/faltas) na variável de estado não são zeradas. | **I**nfo: `reiniciarPartida` |

### Cenário B: Lógica de Liderança e Evento Final

**Objetivo:** Testar o destaque visual de liderança e a geração do resumo final.

| Passo | Ação do Usuário/Sistema | Funcionamento Esperado | Análise de Mal Funcionamento (Bug) | Nível de Log a Acompanhar |
| :--- | :--- | :--- | :--- | :--- |
| **1** | Time A marca 3 pontos. Time B marca 2 pontos. | O **Time A** é destacado (👑) na UI. O log **D**ebug em `destacarLider` confirma o líder. | Nenhum time é destacado ou o destaque não é removido no empate. | **D**ebug: `destacarLider` |
| **2** | Time B marca 1 ponto (Placar A: 3 x B: 3). | O destaque de líder é **removido** (empate). | O Time A continua destacado por ter pontuado primeiro. | **D**ebug: `destacarLider` |
| **3** | Clica no botão **Finalizar Partida**. | O log **I**nfo é gerado. Um **AlertDialog** é exibido mostrando o resumo. | O log **I**nfo está ausente, ou o vencedor é incorretamente reportado. | **I**nfo: `mostrarResultadoFinal` |

## 📄 Licença

Este projeto é desenvolvido para fins educacionais como parte do curso de desenvolvimento mobile.

---

⭐ **Dica**: Se gostou do projeto, não esqueça de dar uma estrela no repositório!

**Última atualização**: Outubro 2025
