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

## 📄 Licença

Este projeto é desenvolvido para fins educacionais como parte do curso de desenvolvimento mobile.

---

⭐ **Dica**: Se gostou do projeto, não esqueça de dar uma estrela no repositório!

**Última atualização**: Outubro 2025
