package com.example.mundoaberto.data.model

enum class WeatherType(val label: String, val icon: String, val description: String) {
    SUNNY("Ensolarado", "☀️", "Céu limpo com luz dourada sobre as praças"),
    GENTLE_RAIN("Chuva Mansa", "🌧️", "Gotas frescas incentivando o aconchego no café"),
    BREEZE("Brisa da Primavera", "🍃", "Vento suave agitando as cerejeiras do parque"),
    FESTIVE("Atmosfera Festiva", "✨", "Lanternas acesas e música suave no ar"),
    STARRY_NIGHT("Noite Estrelada", "🌙", "Serenidade e silêncio convidando ao repouso")
}

data class WorldEvent(
    val id: String,
    val title: String,
    val description: String,
    val triggeredAtTime: String,
    val affectedLocationId: String? = null,
    val icon: String = "📢"
)

data class WorldLogEntry(
    val id: String,
    val timeStr: String,
    val message: String,
    val category: String, // "SIMULATION", "DIALOGUE", "EVENT", "INTERVENTION"
    val agentId: String? = null
)

data class WorldState(
    val dayNumber: Int = 1,
    val dayOfWeek: String = "Segunda-feira",
    val hour: Int = 9,
    val minute: Int = 30,
    val weather: WeatherType = WeatherType.SUNNY,
    val simulationSpeed: Float = 1.0f, // 0 = Pausado, 1 = Normal, 2 = Rápido, 5 = Máximo
    val totalTicks: Long = 0,
    val totalConversations: Int = 0,
    val logs: List<WorldLogEntry> = emptyList(),
    val activeEvents: List<WorldEvent> = emptyList()
) {
    val formattedTime: String
        get() = "%02d:%02d".format(hour, minute)

    val isPaused: Boolean
        get() = simulationSpeed == 0f
}
