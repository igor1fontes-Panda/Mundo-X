package com.example.mundoaberto.data.engine

import com.example.mundoaberto.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import kotlin.random.Random

class TownSimulationEngine {

    private val _worldState = MutableStateFlow(
        WorldState(
            dayNumber = 1,
            dayOfWeek = "Segunda-feira",
            hour = 8,
            minute = 30,
            weather = WeatherType.SUNNY,
            simulationSpeed = 1.0f,
            totalTicks = 0,
            totalConversations = 0,
            logs = listOf(
                WorldLogEntry(
                    id = "init_1",
                    timeStr = "08:30",
                    message = "Simulação do Mundo Aberto iniciada com 5 habitantes autônomos.",
                    category = "SIMULATION"
                )
            ),
            activeEvents = emptyList()
        )
    )
    val worldState: StateFlow<WorldState> = _worldState.asStateFlow()

    private val _agents = MutableStateFlow(createInitialAgents())
    val agents: StateFlow<List<Agent>> = _agents.asStateFlow()

    private val _dialogues = MutableStateFlow(createInitialDialogues())
    val dialogues: StateFlow<List<DialogueSession>> = _dialogues.asStateFlow()

    private var cooldownCounter = 0

    companion object {
        fun createInitialAgents(): List<Agent> {
            val plazaCenter = TownLocation.ALL_LOCATIONS.first { it.id == "plaza" }.center
            val cafeCenter = TownLocation.ALL_LOCATIONS.first { it.id == "cafe" }.center
            val libraryCenter = TownLocation.ALL_LOCATIONS.first { it.id == "library" }.center
            val parkCenter = TownLocation.ALL_LOCATIONS.first { it.id == "park" }.center
            val marketCenter = TownLocation.ALL_LOCATIONS.first { it.id == "market" }.center

            return listOf(
                Agent(
                    id = "clara",
                    name = "Clara Ramos",
                    age = 28,
                    occupation = "Escritora & Pesquisadora",
                    emoji = "✍️",
                    avatarColor = 0xFF8B5CF6, // Purple
                    bio = "Pesquisa histórias antigas do vilarejo e escreve crônicas poéticas. Aprecia café forte e o silêncio da biblioteca.",
                    personalityTraits = listOf("Curiosa", "Observadora", "Gentil", "Pensativa"),
                    coreGoal = "Completar a antologia de contos sobre os fundadores do vale.",
                    currentPriority = PriorityLevel.WORK,
                    currentAction = "Revisando manuscritos e tomando notas na Biblioteca",
                    currentThought = "O silêncio matinal aqui é perfeito para dar voz aos personagens.",
                    locationId = "library",
                    targetLocationId = "library",
                    position = libraryCenter,
                    energy = 88,
                    happiness = 92,
                    hunger = 30,
                    socialNeed = 40,
                    memories = listOf(
                        AgentMemory(
                            id = "m_c1",
                            timestamp = "07:45",
                            type = MemoryType.THOUGHT,
                            description = "Acordei inspirada com a luz entrando na janela do quarto.",
                            importance = 6
                        ),
                        AgentMemory(
                            id = "m_c2",
                            timestamp = "08:10",
                            type = MemoryType.OBSERVATION,
                            description = "Vi Sofia abrindo as portas do Café e senti o aroma de canela fresca.",
                            importance = 5
                        )
                    )
                ),
                Agent(
                    id = "mateus",
                    name = "Mateus Silva",
                    age = 34,
                    occupation = "Mestre Construtor & Marceneiro",
                    emoji = "🛠️",
                    avatarColor = 0xFFF59E0B, // Amber
                    bio = "Engenhoso e cordial, cuida das estruturas de madeira e dos bancos da praça. Gosta de conversas francas e trabalho bem feito.",
                    personalityTraits = listOf("Prático", "Sociável", "Prestativo", "Determinado"),
                    coreGoal = "Construir um novo coreto no centro da Praça até o festival.",
                    currentPriority = PriorityLevel.WORK,
                    currentAction = "Aplainando vigas de carvalho no Mercado & Oficinas",
                    currentThought = "Se encaixar este entalhe com firmeza, o banco resistirá a décadas de chuva.",
                    locationId = "market",
                    targetLocationId = "market",
                    position = marketCenter,
                    energy = 95,
                    happiness = 85,
                    hunger = 40,
                    socialNeed = 60,
                    memories = listOf(
                        AgentMemory(
                            id = "m_m1",
                            timestamp = "07:30",
                            type = MemoryType.OBSERVATION,
                            description = "Chequei as ferramentas da oficina; a serra precisa ser amolada à tarde.",
                            importance = 4
                        )
                    )
                ),
                Agent(
                    id = "sofia",
                    name = "Sofia Mendes",
                    age = 26,
                    occupation = "Barista & Anfitriã do Café",
                    emoji = "☕",
                    avatarColor = 0xFF0D9488, // Teal
                    bio = "Guardiã informal das notícias do vilarejo. Ouve com atenção todos que cruzam o balcão e prepara infusões acolhedoras.",
                    personalityTraits = listOf("Empática", "Comunicativa", "Acolhedora", "Animada"),
                    coreGoal = "Fazer com que ninguém se sinta sozinho na cidade.",
                    currentPriority = PriorityLevel.WORK,
                    currentAction = "Moendo grãos torrados e organizando xícaras no Café",
                    currentThought = "Hoje sinto que muitos passarão por aqui para se aquecer.",
                    locationId = "cafe",
                    targetLocationId = "cafe",
                    position = cafeCenter,
                    energy = 90,
                    happiness = 94,
                    hunger = 25,
                    socialNeed = 75,
                    memories = listOf(
                        AgentMemory(
                            id = "m_s1",
                            timestamp = "08:00",
                            type = MemoryType.THOUGHT,
                            description = "Coloquei flores frescas nos vasinhos de todas as mesas do café.",
                            importance = 5
                        )
                    )
                ),
                Agent(
                    id = "lucas",
                    name = "Lucas Prado",
                    age = 31,
                    occupation = "Botânico & Jardineiro",
                    emoji = "🌿",
                    avatarColor = 0xFF10B981, // Emerald
                    bio = "Conhece cada espécie vegetal e muda da região. Passa horas podando canteiros e meditando junto ao lago.",
                    personalityTraits = listOf("Sereno", "Paciente", "Naturalista", "Sensível"),
                    coreGoal = "Cultivar uma rara orquídea azul que só floresce sob brisa fresca.",
                    currentPriority = PriorityLevel.WORK,
                    currentAction = "Regando os canteiros perto do lago no Parque",
                    currentThought = "A umidade das folhas hoje cedo está ideal para as mudas novas.",
                    locationId = "park",
                    targetLocationId = "park",
                    position = parkCenter,
                    energy = 85,
                    happiness = 89,
                    hunger = 35,
                    socialNeed = 30,
                    memories = listOf(
                        AgentMemory(
                            id = "m_l1",
                            timestamp = "07:15",
                            type = MemoryType.OBSERVATION,
                            description = "Notei que as cerejeiras começaram a abrir os primeiros botões rosados.",
                            importance = 6
                        )
                    )
                ),
                Agent(
                    id = "helena",
                    name = "Helena Costa",
                    age = 42,
                    occupation = "Professora & Astrônoma",
                    emoji = "🔭",
                    avatarColor = 0xFF3B82F6, // Blue
                    bio = "Apaixonada pelos mapas celestes e por história natural. Sempre disposta a debater grandes ideias ou ensinar curiosidades aos vizinhos.",
                    personalityTraits = listOf("Eloquente", "Metódica", "Generosa", "Filosófica"),
                    coreGoal = "Catalogar todas as constelações visíveis do vale durante as quatro estações.",
                    currentPriority = PriorityLevel.LEISURE,
                    currentAction = "Caminhando ao redor do chafariz na Praça Central",
                    currentThought = "O alinhamento solar desta manhã promete um meio-dia radiante.",
                    locationId = "plaza",
                    targetLocationId = "plaza",
                    position = plazaCenter,
                    energy = 92,
                    happiness = 88,
                    hunger = 30,
                    socialNeed = 65,
                    memories = listOf(
                        AgentMemory(
                            id = "m_h1",
                            timestamp = "08:15",
                            type = MemoryType.REFLECTION,
                            description = "A harmonia deste vilarejo nasce da cooperação entre diferentes vocações.",
                            importance = 7
                        )
                    )
                )
            )
        }

        private fun createInitialDialogues(): List<DialogueSession> {
            return listOf(
                DialogueSession(
                    id = "dial_0",
                    timeStr = "08:15",
                    locationId = "cafe",
                    locationName = "Café & Taverna O Encontro",
                    participantIds = listOf("sofia", "clara"),
                    participantNames = listOf("Sofia Mendes", "Clara Ramos"),
                    messages = listOf(
                        DialogueMessage("sofia", "Sofia", "Bom dia, Clara! Café forte como sempre para abrir a imaginação?", "08:15"),
                        DialogueMessage("clara", "Clara", "Exatamente, Sofia! O terceiro capítulo do meu livro precisa de energia pura.", "08:16"),
                        DialogueMessage("sofia", "Sofia", "Aqui está bem quentinho. Depois me conte se os personagens se reconciliaram!", "08:16")
                    ),
                    summary = "Sofia e Clara trocaram gentilezas matinais enquanto Clara pegava sua xícara de café para a escrita."
                )
            )
        }
    }

    fun stepSimulation() {
        val currentWorld = _worldState.value
        if (currentWorld.isPaused) return

        // Advance virtual clock (+2 minutes per tick)
        var newMin = currentWorld.minute + 2
        var newHour = currentWorld.hour
        var newDay = currentWorld.dayNumber
        var newDayOfWeek = currentWorld.dayOfWeek

        if (newMin >= 60) {
            newMin -= 60
            newHour += 1
            if (newHour >= 24) {
                newHour = 0
                newDay += 1
                newDayOfWeek = getNextDay(newDayOfWeek)
            }
        }

        val timeStr = "%02d:%02d".format(newHour, newMin)
        val logsToAdd = mutableListOf<WorldLogEntry>()

        // Update agents
        val updatedAgents = _agents.value.map { agent ->
            updateSingleAgent(agent, newHour, newMin, timeStr, currentWorld, logsToAdd)
        }

        // Check for interactions / dialogues between agents in the same location
        cooldownCounter++
        if (cooldownCounter >= 6) { // Every ~12 virtual minutes
            val groupedByLoc = updatedAgents.groupBy { it.locationId }
            for ((locId, agentsInLoc) in groupedByLoc) {
                if (agentsInLoc.size >= 2 && locId != "residences") {
                    val a1 = agentsInLoc[0]
                    val a2 = agentsInLoc[1]
                    if (!a1.isTalking && !a2.isTalking && a1.energy > 25 && a2.energy > 25) {
                        triggerConversation(a1, a2, locId, timeStr, logsToAdd)
                        cooldownCounter = 0
                        break
                    }
                }
            }
        }

        // Apply updated world state
        val updatedLogs = (logsToAdd + currentWorld.logs).take(60)
        _agents.value = updatedAgents
        _worldState.value = currentWorld.copy(
            dayNumber = newDay,
            dayOfWeek = newDayOfWeek,
            hour = newHour,
            minute = newMin,
            totalTicks = currentWorld.totalTicks + 1,
            logs = updatedLogs
        )
    }

    private fun updateSingleAgent(
        agent: Agent,
        hour: Int,
        minute: Int,
        timeStr: String,
        worldState: WorldState,
        logs: MutableList<WorldLogEntry>
    ): Agent {
        var energy = agent.energy
        var happiness = agent.happiness
        var hunger = (agent.hunger + 1).coerceAtMost(100)
        var socialNeed = (agent.socialNeed + 1).coerceAtMost(100)

        // Energy dynamics
        if (agent.locationId == "residences" && (hour >= 22 || hour < 7 || energy < 30)) {
            energy = (energy + 4).coerceAtMost(100)
            hunger = (hunger - 1).coerceAtLeast(0)
        } else {
            energy = (energy - 1).coerceAtLeast(5)
        }

        // Check priority table
        val (priority, targetLoc, action, thought) = evaluatePriority(
            agent = agent,
            hour = hour,
            energy = energy,
            hunger = hunger,
            socialNeed = socialNeed,
            worldState = worldState
        )

        // Move position towards target location
        val targetLocationObj = TownLocation.ALL_LOCATIONS.firstOrNull { it.id == targetLoc }
            ?: TownLocation.ALL_LOCATIONS.first()
        val targetCoords = targetLocationObj.center

        val currentX = agent.position.x
        val currentY = agent.position.y
        val dx = targetCoords.x - currentX
        val dy = targetCoords.y - currentY
        val dist = kotlin.math.sqrt(dx * dx + dy * dy)

        val newPos: LocationPoint
        val newLocId: String
        if (dist > 0.04f) {
            val step = 0.025f
            val moveX = currentX + (dx / dist) * step
            val moveY = currentY + (dy / dist) * step
            newPos = LocationPoint(moveX, moveY)
            newLocId = agent.locationId // Em trânsito
        } else {
            // Reached target
            newPos = targetCoords
            newLocId = targetLoc
        }

        // Occasionally generate episodic memory
        val memories = agent.memories.toMutableList()
        if (Random.nextInt(50) == 0 && memories.size < 30) {
            memories.add(
                0,
                AgentMemory(
                    id = "mem_${UUID.randomUUID().toString().take(8)}",
                    timestamp = timeStr,
                    type = if (priority == PriorityLevel.REFLECTION) MemoryType.REFLECTION else MemoryType.THOUGHT,
                    description = thought,
                    importance = Random.nextInt(4, 9)
                )
            )
        }

        return agent.copy(
            energy = energy,
            happiness = happiness,
            hunger = hunger,
            socialNeed = socialNeed,
            currentPriority = priority,
            targetLocationId = targetLoc,
            locationId = newLocId,
            position = newPos,
            currentAction = action,
            currentThought = thought,
            memories = memories.take(25)
        )
    }

    private fun evaluatePriority(
        agent: Agent,
        hour: Int,
        energy: Int,
        hunger: Int,
        socialNeed: Int,
        worldState: WorldState
    ): Quadruple<PriorityLevel, String, String, String> {
        // Priority 1: Critical Survival & Rest
        if (energy < 22 || hour >= 23 || hour < 6) {
            return Quadruple(
                PriorityLevel.SURVIVAL,
                "residences",
                "Repousando no aconchego de sua residência para recuperar energias",
                "O corpo pede descanso. Amanhã continuarei minhas tarefas."
            )
        }

        // Check for active global events override
        val activeEvent = worldState.activeEvents.firstOrNull()
        if (activeEvent != null) {
            if (activeEvent.title.contains("Chuva", ignoreCase = true)) {
                return Quadruple(
                    PriorityLevel.LEISURE,
                    "cafe",
                    "Abrigando-se da chuva forte no Café e bebendo algo quente",
                    "Que bom que o Café estava aberto para me acolher do temporal!"
                )
            } else if (activeEvent.title.contains("Festival", ignoreCase = true)) {
                return Quadruple(
                    PriorityLevel.SOCIAL,
                    "plaza",
                    "Celebrando o Festival na Praça Central ao som de boa música",
                    "Que alegria ver todos os vizinhos reunidos na praça!"
                )
            }
        }

        // Priority 2: Work & Occupation (from 08:30 to 17:00)
        if (hour in 9..16 && energy > 35) {
            return when (agent.id) {
                "clara" -> Quadruple(
                    PriorityLevel.WORK,
                    "library",
                    "Catalogando lendas antigas e compondo novas estrofes",
                    "Cada livro nesta estante guarda a memória de quem passou antes de nós."
                )
                "mateus" -> Quadruple(
                    PriorityLevel.WORK,
                    "market",
                    "Esculpindo vigas de madeira nobre e consertando móveis",
                    "Uma madeira bem tratada dura mais que uma vida inteira."
                )
                "sofia" -> Quadruple(
                    PriorityLevel.WORK,
                    "cafe",
                    "Servindo café com canela e ouvindo as histórias dos visitantes",
                    "Um sorriso sincero transforma o dia de qualquer freguês."
                )
                "lucas" -> Quadruple(
                    PriorityLevel.WORK,
                    "park",
                    "Cuidando da terra, adubando orquídeas e podando o jardim",
                    "A paciência das plantas é a maior lição que a natureza nos dá."
                )
                "helena" -> Quadruple(
                    PriorityLevel.WORK,
                    if (hour % 2 == 0) "library" else "plaza",
                    "Anotando observações sobre o clima e revisando teoremas",
                    "O conhecimento só tem valor quando compartilhado com generosidade."
                )
                else -> Quadruple(
                    PriorityLevel.WORK,
                    "plaza",
                    "Trabalhando na praça",
                    "Dia produtivo no vilarejo."
                )
            }
        }

        // Priority 3: Social & Encounters (midday or late afternoon)
        if (socialNeed > 55 || hour in 12..13 || hour in 17..18) {
            val destination = if (agent.id == "sofia") "cafe" else listOf("plaza", "cafe", "park").random()
            return Quadruple(
                PriorityLevel.SOCIAL,
                destination,
                "Encontrando amigos e trocando novidades agradáveis",
                "Quero saber o que os vizinhos andam criando e descobrindo."
            )
        }

        // Priority 4: Leisure & Contemplation (evening)
        if (hour in 19..22) {
            return Quadruple(
                PriorityLevel.LEISURE,
                if (agent.id == "lucas") "park" else "cafe",
                "Desfrutando do entardecer com tranquilidade",
                "A noite chega mansa, trazendo calma para a mente."
            )
        }

        // Priority 5: Reflection
        return Quadruple(
            PriorityLevel.REFLECTION,
            "plaza",
            "Caminhando e refletindo sobre os acontecimentos recentes",
            "Como é gratificante viver em harmonia nesta pequena comunidade."
        )
    }

    private fun triggerConversation(
        a1: Agent,
        a2: Agent,
        locationId: String,
        timeStr: String,
        logs: MutableList<WorldLogEntry>
    ) {
        val locName = TownLocation.ALL_LOCATIONS.firstOrNull { it.id == locationId }?.name ?: "Vilarejo"
        val (m1, m2, m3, summary) = generateDialogueContent(a1, a2, locName)

        val session = DialogueSession(
            id = "dial_${UUID.randomUUID().toString().take(8)}",
            timeStr = timeStr,
            locationId = locationId,
            locationName = locName,
            participantIds = listOf(a1.id, a2.id),
            participantNames = listOf(a1.name, a2.name),
            messages = listOf(
                DialogueMessage(a1.id, a1.name.substringBefore(" "), m1, timeStr),
                DialogueMessage(a2.id, a2.name.substringBefore(" "), m2, timeStr),
                DialogueMessage(a1.id, a1.name.substringBefore(" "), m3, timeStr)
            ),
            summary = summary
        )

        _dialogues.value = (listOf(session) + _dialogues.value).take(40)
        _worldState.value = _worldState.value.copy(
            totalConversations = _worldState.value.totalConversations + 1
        )

        // Add memory to both agents
        val mem1 = AgentMemory(
            id = "mem_${UUID.randomUUID().toString().take(8)}",
            timestamp = timeStr,
            type = MemoryType.DIALOGUE,
            description = "Conversei com ${a2.name} no(a) $locName sobre $summary",
            importance = 7
        )
        val mem2 = AgentMemory(
            id = "mem_${UUID.randomUUID().toString().take(8)}",
            timestamp = timeStr,
            type = MemoryType.DIALOGUE,
            description = "Encontrei ${a1.name} no(a) $locName e partilhamos ideias: $summary",
            importance = 7
        )

        _agents.value = _agents.value.map { ag ->
            when (ag.id) {
                a1.id -> ag.copy(
                    socialNeed = (ag.socialNeed - 35).coerceAtLeast(0),
                    happiness = (ag.happiness + 10).coerceAtMost(100),
                    memories = (listOf(mem1) + ag.memories).take(25)
                )
                a2.id -> ag.copy(
                    socialNeed = (ag.socialNeed - 35).coerceAtLeast(0),
                    happiness = (ag.happiness + 10).coerceAtMost(100),
                    memories = (listOf(mem2) + ag.memories).take(25)
                )
                else -> ag
            }
        }

        logs.add(
            WorldLogEntry(
                id = "log_${UUID.randomUUID().toString().take(8)}",
                timeStr = timeStr,
                message = "Diálogo entre ${a1.name} e ${a2.name} no(a) $locName: \"$summary\"",
                category = "DIALOGUE"
            )
        )
    }

    private fun generateDialogueContent(
        a1: Agent,
        a2: Agent,
        locName: String
    ): Quadruple<String, String, String, String> {
        val pairs = listOf(
            Quadruple(
                "Olá, ${a2.name.substringBefore(" ")}! Que bom cruzar com você aqui no(a) $locName.",
                "Bom dia, ${a1.name.substringBefore(" ")}! Estava justamente pensando no seu trabalho recente.",
                "O ritmo do dia está ótimo, e conversar com você sempre me traz clareza e ânimo!",
                "Troca de impressões sobre projetos do dia e harmonia no vilarejo."
            ),
            Quadruple(
                "${a2.name.substringBefore(" ")}, reparou como a luz desta manhã valorizou os canteiros e praças?",
                "Sim! A atmosfera hoje tem uma tranquilidade muito especial para quem vive aqui.",
                "Concordo plenamente. Pequenos detalhes tornam nosso mundo acolhedor.",
                "Apreciação mútua sobre o clima e a beleza do ambiente local."
            ),
            Quadruple(
                "Como andam as coisas pelo seu lado, ${a2.name.substringBefore(" ")}?",
                "Muito produtivas! Consegui adiantar bastante da minha meta principal da semana.",
                "Fico muito feliz em saber! Se precisar de uma mão amiga, sabe onde me encontrar.",
                "Apoio mútuo e atualização de metas pessoais."
            )
        )
        return pairs.random()
    }

    fun setSimulationSpeed(speed: Float) {
        _worldState.value = _worldState.value.copy(simulationSpeed = speed)
    }

    fun setWeather(weather: WeatherType) {
        val timeStr = _worldState.value.formattedTime
        val log = WorldLogEntry(
            id = "log_${UUID.randomUUID().toString().take(8)}",
            timeStr = timeStr,
            message = "Clima alterado para: ${weather.label} (${weather.icon})",
            category = "EVENT"
        )
        _worldState.value = _worldState.value.copy(
            weather = weather,
            logs = (listOf(log) + _worldState.value.logs).take(60)
        )
    }

    fun triggerGlobalEvent(title: String, description: String, icon: String) {
        val timeStr = _worldState.value.formattedTime
        val event = WorldEvent(
            id = "ev_${UUID.randomUUID().toString().take(8)}",
            title = title,
            description = description,
            triggeredAtTime = timeStr,
            icon = icon
        )
        val log = WorldLogEntry(
            id = "log_${UUID.randomUUID().toString().take(8)}",
            timeStr = timeStr,
            message = "EVENTO MUNDIAL: $title - $description",
            category = "EVENT"
        )
        _worldState.value = _worldState.value.copy(
            activeEvents = listOf(event) + _worldState.value.activeEvents.take(4),
            logs = (listOf(log) + _worldState.value.logs).take(60)
        )

        // Notify all agents via memories
        val updatedAgents = _agents.value.map { agent ->
            val eventMemory = AgentMemory(
                id = "mem_${UUID.randomUUID().toString().take(8)}",
                timestamp = timeStr,
                type = MemoryType.OBSERVATION,
                description = "Testemunhei o evento: '$title'. $description",
                importance = 9
            )
            agent.copy(memories = (listOf(eventMemory) + agent.memories).take(25))
        }
        _agents.value = updatedAgents
    }

    fun sendInterventionMessage(agentId: String, operatorMessage: String): String {
        val agent = _agents.value.firstOrNull { it.id == agentId } ?: return "Habitante não encontrado."
        val timeStr = _worldState.value.formattedTime

        // Generate contextual in-character response
        val response = when (agent.id) {
            "clara" -> "Muito obrigada pelas palavras! Isso se conecta com um trecho antigo que li sobre a perseverança das ideias. Vou refletir sobre isso em minhas anotações."
            "mateus" -> "Entendido, operador! Vou levar essa sugestão em consideração na próxima estrutura que construir aqui na praça. Trabalho com carinho!"
            "sofia" -> "Que mensagem gentil! Vou preparar um bule especial de chá aromático para receber todos com essa energia positiva hoje!"
            "lucas" -> "A natureza escuta tudo com serenidade. Suas palavras me inspiram a cuidar com ainda mais atenção dos brotos no jardim."
            "helena" -> "Uma observação perspicaz! As grandes descobertas começam justamente quando olhamos além do cotidiano. Agradeço a intervenção."
            else -> "Recebi sua mensagem e ela ecoa em meus pensamentos agora."
        }

        val mem = AgentMemory(
            id = "mem_${UUID.randomUUID().toString().take(8)}",
            timestamp = timeStr,
            type = MemoryType.INTERVENTION,
            description = "Mensagem do Operador: \"$operatorMessage\" -> Minha resposta: \"$response\"",
            importance = 10
        )

        val log = WorldLogEntry(
            id = "log_${UUID.randomUUID().toString().take(8)}",
            timeStr = timeStr,
            message = "Intervenção direta com ${agent.name}: \"$operatorMessage\"",
            category = "INTERVENTION",
            agentId = agent.id
        )

        _agents.value = _agents.value.map { ag ->
            if (ag.id == agentId) {
                ag.copy(
                    happiness = (ag.happiness + 15).coerceAtMost(100),
                    memories = (listOf(mem) + ag.memories).take(25)
                )
            } else ag
        }

        _worldState.value = _worldState.value.copy(
            logs = (listOf(log) + _worldState.value.logs).take(60)
        )

        return response
    }

    fun forceAgentDestination(agentId: String, locationId: String) {
        val targetLoc = TownLocation.ALL_LOCATIONS.firstOrNull { it.id == locationId } ?: return
        val timeStr = _worldState.value.formattedTime
        _agents.value = _agents.value.map { ag ->
            if (ag.id == agentId) {
                ag.copy(
                    targetLocationId = locationId,
                    currentAction = "Direcionado pelo operador para o(a) ${targetLoc.name}",
                    currentThought = "Estou me dirigindo ao(a) ${targetLoc.name} agora."
                )
            } else ag
        }

        val log = WorldLogEntry(
            id = "log_${UUID.randomUUID().toString().take(8)}",
            timeStr = timeStr,
            message = "Operador redirecionou habitante para: ${targetLoc.name}",
            category = "INTERVENTION",
            agentId = agentId
        )
        _worldState.value = _worldState.value.copy(
            logs = (listOf(log) + _worldState.value.logs).take(60)
        )
    }

    private fun getNextDay(day: String): String {
        return when (day) {
            "Segunda-feira" -> "Terça-feira"
            "Terça-feira" -> "Quarta-feira"
            "Quarta-feira" -> "Quinta-feira"
            "Quinta-feira" -> "Sexta-feira"
            "Sexta-feira" -> "Sábado"
            "Sábado" -> "Domingo"
            else -> "Segunda-feira"
        }
    }
}

data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
