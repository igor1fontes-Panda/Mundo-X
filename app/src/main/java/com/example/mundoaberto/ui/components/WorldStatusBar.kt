package com.example.mundoaberto.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mundoaberto.data.model.WorldState
import com.example.mundoaberto.ui.theme.*

@Composable
fun WorldStatusBar(
    worldState: WorldState,
    onTogglePause: () -> Unit,
    onSetSpeed: (Float) -> Unit,
    onWeatherClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .testTag("world_status_bar"),
        color = DarkSurface,
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Time & Day Info
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (worldState.isPaused) AccentAmber else AccentEmerald)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = worldState.formattedTime,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Dia ${worldState.dayNumber}",
                                style = MaterialTheme.typography.labelSmall,
                                color = PrimarySky
                            )
                        }
                        Text(
                            text = worldState.dayOfWeek,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                // Weather Pill (Clickable to change weather)
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = DarkSurfaceVariant,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onWeatherClick() }
                        .testTag("weather_pill")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = worldState.weather.icon,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = worldState.weather.label,
                            style = MaterialTheme.typography.labelMedium,
                            color = TextPrimary
                        )
                    }
                }

                // Speed & Playback Controls
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = onTogglePause,
                        modifier = Modifier
                            .size(36.dp)
                            .background(DarkSurfaceVariant, CircleShape)
                            .testTag("play_pause_button")
                    ) {
                        Icon(
                            imageVector = if (worldState.isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                            contentDescription = if (worldState.isPaused) "Iniciar" else "Pausar",
                            tint = if (worldState.isPaused) AccentAmber else PrimarySky,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Speed toggles: 1x, 2x, 5x
                    listOf(1.0f, 2.0f, 5.0f).forEach { speed ->
                        val isCurrent = worldState.simulationSpeed == speed
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isCurrent) PrimarySky else DarkSurfaceVariant)
                                .clickable { onSetSpeed(speed) }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                                .testTag("speed_button_${speed.toInt()}x"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${speed.toInt()}x",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isCurrent) OnPrimary else TextSecondary
                            )
                        }
                    }
                }
            }

            // Global Event Banner if active
            worldState.activeEvents.firstOrNull()?.let { event ->
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = PrimaryContainer.copy(alpha = 0.35f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = event.icon, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${event.title}: ${event.description}",
                            style = MaterialTheme.typography.bodySmall,
                            color = PrimarySky,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
