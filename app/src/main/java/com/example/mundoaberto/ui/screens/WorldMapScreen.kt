package com.example.mundoaberto.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mundoaberto.data.model.*
import com.example.mundoaberto.ui.components.TownMapCanvas
import com.example.mundoaberto.ui.components.WorldStatusBar
import com.example.mundoaberto.ui.theme.*

@Composable
fun WorldMapScreen(
    worldState: WorldState,
    agents: List<Agent>,
    selectedAgentId: String?,
    selectedLocationId: String?,
    onSelectAgent: (String) -> Unit,
    onSelectLocation: (String) -> Unit,
    onOpenAgentDetail: (Agent) -> Unit,
    onTogglePause: () -> Unit,
    onSetSpeed: (Float) -> Unit,
    onWeatherClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedAgent = agents.firstOrNull { it.id == selectedAgentId }
    val selectedLocation = TownLocation.ALL_LOCATIONS.firstOrNull { it.id == selectedLocationId }
        ?: TownLocation.ALL_LOCATIONS.first()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        // 1. World Status Bar (Live Clock, Weather, Speeds)
        item {
            WorldStatusBar(
                worldState = worldState,
                onTogglePause = onTogglePause,
                onSetSpeed = onSetSpeed,
                onWeatherClick = onWeatherClick
            )
        }

        // 2. Interactive Town Map Canvas
        item {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Mapa do Vilarejo em Tempo Real",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Text(
                        text = "Toque nos locais ou avatares",
                        style = MaterialTheme.typography.labelSmall,
                        color = PrimarySky
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                TownMapCanvas(
                    agents = agents,
                    selectedAgentId = selectedAgentId,
                    selectedLocationId = selectedLocationId,
                    onAgentClick = { id -> onSelectAgent(id) },
                    onLocationClick = { id -> onSelectLocation(id) }
                )
            }
        }

        // 3. Quick Agent Selector Bar
        item {
            Text(
                text = "Habitantes Ativos (${agents.size})",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                agents.forEach { agent ->
                    val isSelected = agent.id == selectedAgentId
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) PrimaryContainer else DarkSurfaceVariant,
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, PrimarySky) else null,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onSelectAgent(agent.id) }
                            .testTag("map_agent_chip_${agent.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(agent.avatarColor)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = agent.name.first().toString(), color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = agent.name.substringBefore(" "),
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = if (isSelected) Color.White else TextPrimary
                                )
                                Text(
                                    text = agent.occupation.take(12),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) PrimarySky else TextSecondary,
                                    fontSize = 9.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. Selected Agent Quick Card
        item {
            selectedAgent?.let { agent ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("selected_agent_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = agent.emoji, fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = agent.name,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = agent.occupation,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = PrimarySky
                                    )
                                }
                            }

                            Button(
                                onClick = { onOpenAgentDetail(agent) },
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("inspect_agent_button")
                            ) {
                                Text("Inspecionar", fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DarkSurfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "Atividade Atual:",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary
                                )
                                Text(
                                    text = agent.currentAction,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "\"${agent.currentThought}\"",
                                    style = MaterialTheme.typography.bodySmall.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                                    color = TertiaryViolet
                                )
                            }
                        }
                    }
                }
            }
        }

        // 5. Selected Location Card
        item {
            val agentsInLocation = agents.filter { it.locationId == selectedLocation.id }
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("selected_location_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = SecondaryTeal,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = selectedLocation.name,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = DarkSurfaceHighlight
                        ) {
                            Text(
                                text = "${agentsInLocation.size} presente(s)",
                                style = MaterialTheme.typography.labelSmall,
                                color = SecondaryTeal,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = selectedLocation.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    if (agentsInLocation.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "No local:",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                            agentsInLocation.forEach { ag ->
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = DarkSurface,
                                    modifier = Modifier.clickable { onSelectAgent(ag.id) }
                                ) {
                                    Text(
                                        text = "${ag.emoji} ${ag.name.substringBefore(" ")}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextPrimary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
