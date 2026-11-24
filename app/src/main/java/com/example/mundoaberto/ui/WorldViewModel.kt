package com.example.mundoaberto.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mundoaberto.data.engine.TownSimulationEngine
import com.example.mundoaberto.data.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class WorldViewModel : ViewModel() {

    private val engine = TownSimulationEngine()

    val worldState: StateFlow<WorldState> = engine.worldState
    val agents: StateFlow<List<Agent>> = engine.agents
    val dialogues: StateFlow<List<DialogueSession>> = engine.dialogues

    private val _selectedAgentId = MutableStateFlow<String?>("clara")
    val selectedAgentId: StateFlow<String?> = _selectedAgentId.asStateFlow()

    private val _selectedLocationId = MutableStateFlow<String?>("plaza")
    val selectedLocationId: StateFlow<String?> = _selectedLocationId.asStateFlow()

    private var simulationJob: Job? = null

    init {
        startSimulationLoop()
    }

    private fun startSimulationLoop() {
        simulationJob?.cancel()
        simulationJob = viewModelScope.launch {
            while (isActive) {
                val speed = worldState.value.simulationSpeed
                if (speed > 0f) {
                    val delayMillis = (1500L / speed).toLong().coerceAtLeast(300L)
                    delay(delayMillis)
                    engine.stepSimulation()
                } else {
                    delay(500L)
                }
            }
        }
    }

    fun setSpeed(speed: Float) {
        engine.setSimulationSpeed(speed)
    }

    fun togglePause() {
        val currentSpeed = worldState.value.simulationSpeed
        if (currentSpeed == 0f) {
            engine.setSimulationSpeed(1.0f)
        } else {
            engine.setSimulationSpeed(0f)
        }
    }

    fun setWeather(weather: WeatherType) {
        engine.setWeather(weather)
    }

    fun triggerGlobalEvent(title: String, description: String, icon: String = "📢") {
        engine.triggerGlobalEvent(title, description, icon)
    }

    fun selectAgent(agentId: String?) {
        _selectedAgentId.value = agentId
    }

    fun selectLocation(locationId: String?) {
        _selectedLocationId.value = locationId
    }

    fun sendDirectMessage(agentId: String, message: String): String {
        return engine.sendInterventionMessage(agentId, message)
    }

    fun redirectAgent(agentId: String, locationId: String) {
        engine.forceAgentDestination(agentId, locationId)
    }
}
