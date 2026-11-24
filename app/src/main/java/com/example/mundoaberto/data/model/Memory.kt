package com.example.mundoaberto.data.model

enum class MemoryType {
    THOUGHT,      // Pensamento interno
    OBSERVATION,  // Observação do ambiente ou evento
    DIALOGUE,     // Conversa com outro habitante
    REFLECTION,   // Síntese de longo prazo
    INTERVENTION  // Interação direta com o operador
}

data class AgentMemory(
    val id: String,
    val timestamp: String,
    val type: MemoryType,
    val description: String,
    val importance: Int = 5 // 1 a 10
)

data class DialogueMessage(
    val senderId: String,
    val senderName: String,
    val text: String,
    val timeStr: String
)

data class DialogueSession(
    val id: String,
    val timeStr: String,
    val locationId: String,
    val locationName: String,
    val participantIds: List<String>,
    val participantNames: List<String>,
    val messages: List<DialogueMessage>,
    val summary: String
)
