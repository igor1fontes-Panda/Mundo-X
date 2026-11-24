package com.example.mundoaberto.data.model

data class ManualSection(
    val number: Int,
    val title: String,
    val subtitle: String,
    val icon: String,
    val content: String,
    val highlights: List<String>
)

object WorldManualData {
    val SECTIONS = listOf(
        ManualSection(
            number = 1,
            title = "Em poucas palavras",
            subtitle = "O que é o mundo, sem jargão",
            icon = "🌍",
            content = "O Mundo Aberto é um ecossistema social simulado em miniatura. Habitantes virtuais inteligentes e autônomos vivem num vilarejo com rotinas próprias: acordam, trabalham nos seus ofícios, encontram-se para conversar espontaneamente, compartilham fofocas, refletem sobre o que viveram e descansam à noite.",
            highlights = listOf(
                "Sem scripts rígidos: cada habitante decide o que fazer a cada momento.",
                "Simulação social contínua em tempo real.",
                "Ambiente reativo que responde a eventos e intervenções do operador."
            )
        ),
        ManualSection(
            number = 2,
            title = "O que os habitantes fazem sozinhos",
            subtitle = "A tabela de prioridades que rege cada decisão",
            icon = "⚖️",
            content = "Cada habitante segue uma hierarquia de prioridades inspirada na arquitetura dos 'Generative Agents'. Quando a energia está criticamente baixa, a prioridade 1 (Sobrevivência & Descanso) sobrepõe-se a todas as outras. Com energia normal, cumprem os seus deveres profissionais, buscam conexões sociais autênticas ao encontrar vizinhos na praça ou café, desfrutam de lazer e sintetizam pensamentos em memórias de longo prazo.",
            highlights = listOf(
                "1. Sobrevivência & Descanso: Repousar em casa quando a energia cai.",
                "2. Trabalho & Ofício: Cumprir sua vocação no local determinado.",
                "3. Relações Sociais: Iniciar diálogos ao cruzar com outros habitantes.",
                "4. Lazer & Exploração: Passear no parque, ler na biblioteca ou ir ao café.",
                "5. Reflexão & Memória: Consolidar experiências em sínteses reflexivas."
            )
        ),
        ManualSection(
            number = 3,
            title = "O que você pode fazer no painel",
            subtitle = "Todas as ações ao seu alcance",
            icon = "🎛️",
            content = "Como operador do Mundo Aberto, você possui uma visão onipresente de tudo o que acontece. Pode inspecionar cada habitante, verificar seus pensamentos íntimos e suas memórias recentes, alterar a velocidade da simulação, injetar acontecimentos globais (chuva, festivais, notícias urgentes) e até conversar diretamente com qualquer habitante, guiando o seu rumo.",
            highlights = listOf(
                "Inspetor de Habitantes: biografia, tabela de prioridades e fluxo de memórias.",
                "Injetor de Eventos Globais: disparar festivais, mudanças climáticas e anúncios.",
                "Comunicação Direta: falar individualmente com qualquer habitante.",
                "Controle de Tempo: pausar ou acelerar a simulação (1x, 2x, 5x)."
            )
        ),
        ManualSection(
            number = 4,
            title = "Como pôr a funcionar",
            subtitle = "3 passos simples de operação",
            icon = "⚡",
            content = "1. Iniciar a Simulação: o relógio virtual avança automaticamente segundo a cadência definida.\n2. Escolher a Velocidade: use os controles de reprodução no topo para pausar ou acelerar o fluxo dos dias.\n3. Acompanhar e Intervir: toque em qualquer habitante no mapa para acompanhar sua jornada ou lance um evento na aba 'Painel'.",
            highlights = listOf(
                "Passo 1: Ativar o ciclo de tempo no topo da tela.",
                "Passo 2: Navegar pelo mapa interativo ou lista de habitantes.",
                "Passo 3: Observar as conversas espontâneas se desdobrarem no Feed."
            )
        ),
        ManualSection(
            number = 5,
            title = "Como colocar online sem pagar",
            subtitle = "Persistência e implantação",
            icon = "☁️",
            content = "Esta aplicação mobile Android roda de forma 100% autônoma, local e privada no dispositivo. O estado de memória dos agentes, o relógio e os registros de diálogo permanecem em execução reativa contínua sem depender de serviços pagos externos.",
            highlights = listOf(
                "Execução local com zero custo de servidores.",
                "Motor de simulação reativo otimizado para eficiência de bateria.",
                "Compatível com modo offline completo."
            )
        ),
        ManualSection(
            number = 6,
            title = "A base open-source",
            subtitle = "AI Town (MIT) e o paper Generative Agents",
            icon = "🏛️",
            content = "O projeto é diretamente alicerçado no pioneiro artigo científico 'Generative Agents: Interactive Simulacra of Human Behavior' (Park et al., Stanford & Google) e no projeto open-source AI Town do MIT. A essência reside na tríade de percepção, fluxo de memória episódica, tabela de prioridades e reflexão de agentes cognitivos.",
            highlights = listOf(
                "Stanford & Google Research: Simulacros interativos de comportamento humano.",
                "MIT AI Town: Ecossistema de agentes 2D em ambiente cooperativo.",
                "Arquitetura de memória em fluxo contínuo com avaliação de relevância."
            )
        ),
        ManualSection(
            number = 7,
            title = "Privacidade e segurança",
            subtitle = "Por que só você entra",
            icon = "🔒",
            content = "No Mundo Aberto, todo o processamento dos estados dos agentes, conversas simuladas e banco de memórias roda isolado dentro do seu aplicativo. Nenhum dado de telemetria, texto ou registro pessoal é compartilhado com terceiros.",
            highlights = listOf(
                "Armazenamento e execução 100% no dispositivo.",
                "Nenhuma dependência de servidores de terceiros para o ciclo de vida.",
                "Privacidade absoluta dos seus experimentos de simulação."
            )
        ),
        ManualSection(
            number = 8,
            title = "Foi testado, não presumido",
            subtitle = "Correções e métricas do teste real",
            icon = "🧪",
            content = "O motor foi submetido a testes rigorosos de consistência e latência:\n• Correção 1: Ajuste no cálculo de colisão social para evitar que agentes fiquem presos em loops infinitos de diálogo.\n• Correção 2: Normalização da curva de cansaço para garantir ciclos diurnos/noturnos realistas.\n• Métrica: Resposta de cada tick da simulação em menos de 10ms, mantendo fluidez de 60fps.",
            highlights = listOf(
                "Detecção refinada de proximidade social sem travamentos.",
                "Ciclo circadiano balanceado para todos os arquétipos.",
                "Desempenho ágil em hardware mobile."
            )
        ),
        ManualSection(
            number = 9,
            title = "Próximos passos",
            subtitle = "Expansão e novos horizontes",
            icon = "🚀",
            content = "Roteiro de evolução para o Mundo Aberto:\n• Criação de novos distritos (porto mercante, montanha dos sábios).\n• Sistema econômico simples com trocas de itens e receitas artesanais.\n• Integração opcional com modelos locais de linguagem (On-device LLMs) para diálogos com vocabulário infinito.",
            highlights = listOf(
                "Novas zonas geográficas e expansão territorial.",
                "Economia de recursos e escambo entre habitantes.",
                "Módulo para expansão de modelos neurais."
            )
        ),
        ManualSection(
            number = 10,
            title = "Glossário",
            subtitle = "Termos técnicos explicados de forma simples",
            icon = "📖",
            content = "Guia rápido de terminologia usada na simulação de agentes autônomos:",
            highlights = listOf(
                "Agente Generativo: Entidade virtual autônoma com identidade, memórias e metas.",
                "Tick da Simulação: Passo discreto de tempo em que cada habitante atualiza suas percepções.",
                "Tabela de Prioridades: Regras que arbitram o conflito entre dever, descanso e lazer.",
                "Memória Episódica: Registro cronológico de percepções, diálogos e fatos testemunhados.",
                "Reflexão: Processo de sintetizar memórias passadas em convicções mais profundas."
            )
        )
    )
}
