package com.example.mundoaberto.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.mundoaberto.data.model.WeatherType
import com.example.mundoaberto.data.model.WorldState
import com.example.mundoaberto.ui.theme.*

@Composable
fun ControlPanelScreen(
    worldState: WorldState,
    onSetWeather: (WeatherType) -> Unit,
    onSetSpeed: (Float) -> Unit,
    onTriggerEvent: (String, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var customEventTitle by remember { mutableStateOf("") }
    var customEventDesc by remember { mutableStateOf("") }
    var showEventSuccess by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // Title
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = null,
                        tint = PrimarySky,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Painel do Operador (Modo Deus)",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ações de intervenção, controle do tempo, clima e injeção de eventos mundiais",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }

        // 1. Weather Control
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Controle Climático do Mundo",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        WeatherType.values().forEach { weather ->
                            val isSelected = worldState.weather == weather
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) PrimaryContainer else DarkSurfaceVariant,
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, PrimarySky) else null,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { onSetWeather(weather) }
                                    .testTag("weather_btn_${weather.name}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = weather.icon, fontSize = 18.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = weather.label,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (isSelected) Color.White else TextPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. Preset Global Events Injector
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Injetor de Acontecimentos Globais",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = AccentAmber
                    )
                    Text(
                        text = "Dispare acontecimentos coletivos. Todos os habitantes perceberão e guardarão em suas memórias:",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    // Preset buttons
                    val presets = listOf(
                        Triple("Festival da Colheita na Praça", "Música e dança convocam todos os habitantes para comemorar.", "🎉"),
                        Triple("Chuva Torrencial no Vale", "Gotas grossas e trovões forçam todos a buscarem abrigo seguro.", "⛈️"),
                        Triple("Feira de Artesanato & Trocas", "Artesãos e moradores expõem novidades no Mercado Central.", "🏺"),
                        Triple("Noite de Poesia & Histórias", "Uma tertúlia aconchegante com café quente e leituras.", "📖")
                    )

                    presets.forEach { (title, desc, icon) ->
                        OutlinedButton(
                            onClick = {
                                onTriggerEvent(title, desc, icon)
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("preset_event_${title.take(8)}")
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = icon, fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = title,
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = desc,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }

                    Divider(color = OutlineBorder, modifier = Modifier.padding(vertical = 4.dp))

                    // Custom Event Form
                    Text(
                        text = "Criar Evento Personalizado:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = PrimarySky
                    )
                    OutlinedTextField(
                        value = customEventTitle,
                        onValueChange = { customEventTitle = it },
                        placeholder = { Text("Título do evento (ex: Inauguração do Coreto)", fontSize = 13.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("custom_event_title_input"),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = customEventDesc,
                        onValueChange = { customEventDesc = it },
                        placeholder = { Text("Descrição detalhada do que aconteceu no vilarejo...", fontSize = 13.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("custom_event_desc_input"),
                        shape = RoundedCornerShape(10.dp),
                        maxLines = 2
                    )
                    Button(
                        onClick = {
                            if (customEventTitle.isNotBlank()) {
                                onTriggerEvent(
                                    customEventTitle,
                                    if (customEventDesc.isBlank()) "Evento transmitido pelo operador." else customEventDesc,
                                    "📢"
                                )
                                customEventTitle = ""
                                customEventDesc = ""
                                showEventSuccess = true
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("broadcast_custom_event_button")
                    ) {
                        Icon(Icons.Default.Campaign, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Transmitir Evento ao Vilarejo", fontSize = 13.sp)
                    }

                    if (showEventSuccess) {
                        Text(
                            text = "✓ Evento transmitido com sucesso a todos os habitantes!",
                            style = MaterialTheme.typography.labelSmall,
                            color = AccentEmerald
                        )
                    }
                }
            }
        }

        // 3. Telemetry & Simulation Metrics
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Métricas & Telemetria do Motor",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MetricBox("Tiques Totais", "${worldState.totalTicks}", PrimarySky)
                        MetricBox("Diálogos Gerados", "${worldState.totalConversations}", SecondaryTeal)
                        MetricBox("Velocidade", if (worldState.isPaused) "Pausa" else "${worldState.simulationSpeed.toInt()}x", AccentAmber)
                    }
                }
            }
        }

        // 4. World Log Console
        item {
            Text(
                text = "Console de Logs do Mundo (${worldState.logs.size})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
        }

        items(worldState.logs, key = { it.id }) { log ->
            val badgeColor = when (log.category) {
                "DIALOGUE" -> SecondaryTeal
                "EVENT" -> AccentAmber
                "INTERVENTION" -> AccentRose
                else -> PrimarySky
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = DarkSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = log.timeStr,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = badgeColor.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = log.category.take(4),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = badgeColor,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = log.message,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricBox(label: String, value: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = DarkSurfaceVariant,
        modifier = Modifier.width(100.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = color)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        }
    }
}
