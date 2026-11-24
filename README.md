# Mundo Aberto 🌍

**Mundo Aberto** é uma simulação social autônoma e interativa para Android desenvolvida em **Kotlin** e **Jetpack Compose**, inspirada na arquitetura dos *Generative Agents* (Stanford/Google) e no *AI Town* (MIT).

---

## 🏛️ Funcionalidades Principais

1. **Mapa Interativo 2D do Vilarejo**:
   - Canvas vetorial renderizando a Praça Central com chafariz animado, Café & Taverna, Biblioteca & Arquivos, Parque das Cerejeiras com lago, Mercado & Oficinas e a Vila Residencial.
   - Avatares animados transitando dinamicamente pelo mapa com posições interpoladas.
   - Seleção interativa por toque em qualquer habitante ou local.

2. **Habitantes Autônomos (Generative Agents)**:
   - **Clara Ramos** (Escritora & Pesquisadora)
   - **Mateus Silva** (Mestre Construtor & Marceneiro)
   - **Sofia Mendes** (Barista & Anfitriã do Café)
   - **Lucas Prado** (Botânico & Jardineiro)
   - **Helena Costa** (Professora & Astrônoma)
   - **Tabela de Prioridades**: Arbitra dinamicamente entre Sobrevivência/Descanso, Trabalho/Ofício, Relações Sociais, Lazer/Exploração e Reflexão.
   - **Necessidades Vitais**: Energia, Felicidade, Saciedade e Conexão Social.
   - **Fluxo de Memórias Episódicas**: Registro cronológico de pensamentos, observações, diálogos e reflexões sintetizadas.

3. **Feed de Diálogos & Encontros**:
   - Diálogos espontâneos gerados organicamente quando dois habitantes se encontram no mesmo local.
   - Transcrição completa com balões de fala, resumo e consolidação nas memórias dos participantes.

4. **Painel do Operador (Modo Deus)**:
   - Controle do tempo virtual com relógio circadiano e velocidades (Pausa, 1x, 2x, 5x).
   - Mudança climática em tempo real (Ensolarado, Chuva Mansa, Brisa de Primavera, Atmosfera Festiva, Noite Estrelada).
   - Injetor de Acontecimentos Globais (Festivais, Tempestades, Feiras de Artesanato) e criador de eventos personalizados com transmissão instantânea.
   - Intervenção direta com qualquer habitante via diálogo contextual e redirecionamento de tarefas.
   - Console de telemetria e logs da simulação.

5. **Manual Integrado (10 Seções)**:
   - Todas as 10 seções do manual acessível com busca interativa e glossário de termos técnicos.

---

## 🛠️ Tecnologias Utilizadas

- **Kotlin 2.2**
- **Jetpack Compose & Material 3 (M3)**
- **Android Gradle Plugin (AGP) 9.1.1**
- **Gradle 9.3.1**
- **Arquitetura MVVM** com `StateFlow` e Coroutines
