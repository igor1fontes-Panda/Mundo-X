package com.example.mundoaberto.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mundoaberto.data.model.Agent
import com.example.mundoaberto.data.model.WeatherType
import com.example.mundoaberto.ui.components.AgentDetailSheet
import com.example.mundoaberto.ui.screens.*
import com.example.mundoaberto.ui.theme.*

enum class AppTab(val title: String, val icon: ImageVector) {
    MAP("Mapa", Icons.Default.Map),
    INHABITANTS("Habitantes", Icons.Default.People),
    FEED("Diálogos", Icons.Default.Forum),
    CONTROL("Painel", Icons.Default.Tune),
    MANUAL("Manual", Icons.Default.MenuBook)
}

@Composable
fun MainScreen(
    viewModel: WorldViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(AppTab.MAP) }
    var inspectedAgent by remember { mutableStateOf<Agent?>(null) }
    var showWeatherDialog by remember { mutableStateOf(false) }

    val worldState by viewModel.worldState.collectAsStateWithLifecycle()
    val agents by viewModel.agents.collectAsStateWithLifecycle()
    val dialogues by viewModel.dialogues.collectAsStateWithLifecycle()
    val selectedAgentId by viewModel.selectedAgentId.collectAsStateWithLifecycle()
    val selectedLocationId by viewModel.selectedLocationId.collectAsStateWithLifecycle()

    // Keep inspectedAgent in sync with real-time updates
    val currentInspectedAgent = inspectedAgent?.let { current ->
        agents.firstOrNull { it.id == current.id } ?: current
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(
                containerColor = DarkSurface,
                tonalElevation = 8.dp,
                windowInsets = WindowInsets.navigationBars,
                modifier = Modifier.testTag("main_bottom_nav")
            ) {
                AppTab.values().forEach { tab ->
                    val isSelected = selectedTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTab = tab },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title
                            )
                        },
                        label = {
                            Text(text = tab.title, fontSize = 11.sp)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = OnPrimary,
                            selectedTextColor = PrimarySky,
                            indicatorColor = PrimarySky,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        ),
                        modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                AppTab.MAP -> WorldMapScreen(
                    worldState = worldState,
                    agents = agents,
                    selectedAgentId = selectedAgentId,
                    selectedLocationId = selectedLocationId,
                    onSelectAgent = { id -> viewModel.selectAgent(id) },
                    onSelectLocation = { id -> viewModel.selectLocation(id) },
                    onOpenAgentDetail = { agent -> inspectedAgent = agent },
                    onTogglePause = { viewModel.togglePause() },
                    onSetSpeed = { speed -> viewModel.setSpeed(speed) },
                    onWeatherClick = { showWeatherDialog = true }
                )
                AppTab.INHABITANTS -> InhabitantsScreen(
                    agents = agents,
                    onSelectAgent = { agent -> inspectedAgent = agent }
                )
                AppTab.FEED -> DialoguesFeedScreen(
                    dialogues = dialogues
                )
                AppTab.CONTROL -> ControlPanelScreen(
                    worldState = worldState,
                    onSetWeather = { weather -> viewModel.setWeather(weather) },
                    onSetSpeed = { speed -> viewModel.setSpeed(speed) },
                    onTriggerEvent = { title, desc, icon ->
                        viewModel.triggerGlobalEvent(title, desc, icon)
                    }
                )
                AppTab.MANUAL -> DocumentationScreen()
            }
        }

        // Agent Detail Modal Sheet
        currentInspectedAgent?.let { agent ->
            AgentDetailSheet(
                agent = agent,
                onDismiss = { inspectedAgent = null },
                onSendMessage = { msg -> viewModel.sendDirectMessage(agent.id, msg) },
                onRedirect = { locId -> viewModel.redirectAgent(agent.id, locId) }
            )
        }

        // Weather Selection Dialog
        if (showWeatherDialog) {
            AlertDialog(
                onDismissRequest = { showWeatherDialog = false },
                containerColor = DarkSurface,
                title = {
                    Text(
                        text = "Escolha o Clima do Vilarejo",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        WeatherType.values().forEach { weather ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (worldState.weather == weather) PrimaryContainer else DarkSurfaceVariant,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        viewModel.setWeather(weather)
                                        showWeatherDialog = false
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = weather.icon, fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = weather.label,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = weather.description,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextSecondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showWeatherDialog = false }) {
                        Text("Fechar", color = PrimarySky)
                    }
                }
            )
        }
    }
}
