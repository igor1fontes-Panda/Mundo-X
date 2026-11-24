package com.example.mundoaberto.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
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
import com.example.mundoaberto.ui.theme.*

@Composable
fun InhabitantsScreen(
    agents: List<Agent>,
    onSelectAgent: (Agent) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedPriorityFilter by remember { mutableStateOf<PriorityLevel?>(null) }

    val filteredAgents = agents.filter { agent ->
        val matchesSearch = agent.name.contains(searchQuery, ignoreCase = true) ||
                agent.occupation.contains(searchQuery, ignoreCase = true)
        val matchesPriority = selectedPriorityFilter == null || agent.currentPriority == selectedPriorityFilter
        matchesSearch && matchesPriority
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // 1. Header & Title
        item {
            Column {
                Text(
                    text = "Habitantes de Mundo Aberto",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                Text(
                    text = "Simulacros humanos autônomos com memória e tabela de prioridades",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }

        // 2. Search Field
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar habitante por nome ou vocação...", fontSize = 13.sp) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary)
                },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("inhabitants_search_input"),
                singleLine = true
            )
        }

        // 3. Priority Filter Chips
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedPriorityFilter == null,
                    onClick = { selectedPriorityFilter = null },
                    label = { Text("Todos (${agents.size})", fontSize = 12.sp) }
                )
                PriorityLevel.values().forEach { priority ->
                    FilterChip(
                        selected = selectedPriorityFilter == priority,
                        onClick = {
                            selectedPriorityFilter = if (selectedPriorityFilter == priority) null else priority
                        },
                        label = { Text(priority.label.substringBefore("&").trim(), fontSize = 12.sp) }
                    )
                }
            }
        }

        // 4. List of Inhabitants Cards
        items(filteredAgents, key = { it.id }) { agent ->
            val locationName = TownLocation.ALL_LOCATIONS.firstOrNull { it.id == agent.locationId }?.name ?: agent.locationId

            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("agent_card_${agent.id}")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Avatar & Identification Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color(agent.avatarColor)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = agent.emoji, fontSize = 24.sp)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
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

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DarkSurfaceVariant
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = SecondaryTeal,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = locationName.substringBefore("&").substringBefore(" ").take(10),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Current Action & Thought
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = DarkSurfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Prioridade Ativa:",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AccentAmber
                                )
                                Text(
                                    text = agent.currentPriority.label,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = AccentAmber
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
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

                    Spacer(modifier = Modifier.height(10.dp))

                    // Mini Vitals Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MiniVital("⚡ Energia: ${agent.energy}%", agent.energy, PrimarySky)
                        MiniVital("😊 Humor: ${agent.happiness}%", agent.happiness, SecondaryTeal)
                        MiniVital("🧠 Memórias: ${agent.memories.size}", 100, AccentAmber)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Action Button
                    Button(
                        onClick = { onSelectAgent(agent) },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("inspect_agent_btn_${agent.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Ver Dossiê, Memórias & Intervir", fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniVital(label: String, value: Int, color: Color) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = DarkSurfaceHighlight
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
