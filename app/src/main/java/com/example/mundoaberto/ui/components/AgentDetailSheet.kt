package com.example.mundoaberto.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentDetailSheet(
    agent: Agent,
    onDismiss: () -> Unit,
    onSendMessage: (String) -> String,
    onRedirect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var messageInput by remember { mutableStateOf("") }
    var lastResponse by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        modifier = modifier.testTag("agent_detail_sheet")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Agent Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color(agent.avatarColor)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = agent.emoji, fontSize = 28.sp)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = agent.name,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Text(
                            text = "${agent.occupation} • ${agent.age} anos",
                            style = MaterialTheme.typography.bodyMedium,
                            color = PrimarySky
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DarkSurfaceVariant
                        ) {
                            Text(
                                text = "Prioridade: ${agent.currentPriority.label}",
                                style = MaterialTheme.typography.labelSmall,
                                color = AccentAmber,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }

            // 2. Personality traits
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    agent.personalityTraits.forEach { trait ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = DarkSurfaceHighlight
                        ) {
                            Text(
                                text = "• $trait",
                                style = MaterialTheme.typography.labelMedium,
                                color = TextPrimary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }

            // 3. Bio & Core Goal Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Biografia & Personalidade",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = agent.bio,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Meta Central da Vida:",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = SecondaryTeal
                        )
                        Text(
                            text = agent.coreGoal,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextPrimary
                        )
                    }
                }
            }

            // 4. Current State & Vitals (Needs)
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Estado Atual & Necessidades Vitais",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextSecondary
                        )

                        // Action & Thought
                        Column {
                            Text(
                                text = "Ação Corrente:",
                                style = MaterialTheme.typography.labelSmall,
                                color = PrimarySky
                            )
                            Text(
                                text = agent.currentAction,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextPrimary
                            )
                        }

                        Column {
                            Text(
                                text = "Pensamento Íntimo:",
                                style = MaterialTheme.typography.labelSmall,
                                color = TertiaryViolet
                            )
                            Text(
                                text = "\"${agent.currentThought}\"",
                                style = MaterialTheme.typography.bodyMedium.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                                color = TextPrimary
                            )
                        }

                        Divider(color = OutlineBorder)

                        // Vitals Bars
                        VitalStatRow("⚡ Energia", agent.energy, PrimarySky)
                        VitalStatRow("😊 Felicidade", agent.happiness, SecondaryTeal)
                        VitalStatRow("🥖 Saciedade", 100 - agent.hunger, AccentAmber)
                        VitalStatRow("💬 Conexão Social", 100 - agent.socialNeed, TertiaryViolet)
                    }
                }
            }

            // 5. Operator Direct Intervention (Chat with Agent)
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Intervenção Direta do Operador",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = AccentRose
                        )
                        Text(
                            text = "Converse em tempo real com ${agent.name.substringBefore(" ")}. O habitante responderá com base em sua personalidade e memórias:",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = messageInput,
                                onValueChange = { messageInput = it },
                                placeholder = { Text("Ex: Como está sendo seu dia hoje?", fontSize = 13.sp) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("intervention_input"),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = {
                                    if (messageInput.isNotBlank()) {
                                        val reply = onSendMessage(messageInput)
                                        lastResponse = reply
                                        messageInput = ""
                                    }
                                },
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(PrimarySky, RoundedCornerShape(12.dp))
                                    .testTag("send_intervention_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "Enviar",
                                    tint = OnPrimary
                                )
                            }
                        }

                        // Last response
                        lastResponse?.let { reply ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = PrimaryContainer.copy(alpha = 0.3f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = "${agent.name.substringBefore(" ")} respondeu:",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = PrimarySky,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "\"$reply\"",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextPrimary
                                    )
                                }
                            }
                        }

                        // Quick Redirection buttons
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Redirecionar destino agora:",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(
                                "plaza" to "Praça Central",
                                "cafe" to "Café O Encontro",
                                "library" to "Biblioteca",
                                "park" to "Parque",
                                "residences" to "Ir Descansar"
                            ).forEach { (locId, locName) ->
                                OutlinedButton(
                                    onClick = { onRedirect(locId) },
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                    modifier = Modifier.testTag("redirect_${locId}")
                                ) {
                                    Text(text = locName, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }

            // 6. Episodic Memory Stream
            item {
                Text(
                    text = "Fluxo de Memórias Episódicas (${agent.memories.size})",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
            }

            items(agent.memories) { memory ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceHighlight),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = when (memory.type) {
                                MemoryType.THOUGHT -> "💭"
                                MemoryType.OBSERVATION -> "👀"
                                MemoryType.DIALOGUE -> "💬"
                                MemoryType.REFLECTION -> "💡"
                                MemoryType.INTERVENTION -> "⚡"
                            },
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = memory.timestamp,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = PrimarySky
                                )
                                Text(
                                    text = "Relevância: ${memory.importance}/10",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextMuted
                                )
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = memory.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VitalStatRow(label: String, value: Int, color: Color) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            Text(text = "$value%", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = color)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { (value / 100f).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = DarkSurfaceHighlight
        )
    }
}
