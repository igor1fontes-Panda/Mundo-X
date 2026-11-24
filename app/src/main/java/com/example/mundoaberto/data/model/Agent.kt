package com.example.mundoaberto.data.model

enum class PriorityLevel(val label: String, val description: String) {
    SURVIVAL("Sobrevivência & Descanso", "Necessidades biológicas críticas (dormir, alimentar, recarregar energia)"),
    WORK("Trabalho & Ofício", "Exercício da vocação no posto de trabalho ou ateliê"),
    SOCIAL("Relações Sociais", "Conversas espontâneas, troca de novidades e fortalecimento de laços"),
    LEISURE("Lazer & Exploração", "Passeios na praça, leitura na biblioteca ou relaxar no café"),
    REFLECTION("Reflexão & Memória", "Consolidação de experiências e formulação de novos planos")
}

data class Agent(
    val id: String,
    val name: String,
    val age: Int,
    val occupation: String,
    val emoji: String,
    val avatarColor: Long, // Color ARGB
    val bio: String,
    val personalityTraits: List<String>,
    val coreGoal: String,
    val currentPriority: PriorityLevel,
    val currentAction: String,
    val currentThought: String,
    val locationId: String,
    val targetLocationId: String,
    val position: LocationPoint,
    val energy: Int,        // 0-100
    val happiness: Int,     // 0-100
    val hunger: Int,        // 0-100
    val socialNeed: Int,    // 0-100
    val memories: List<AgentMemory> = emptyList(),
    val isTalking: Boolean = false,
    val talkingWithId: String? = null,
    val customNotes: String = ""
)
