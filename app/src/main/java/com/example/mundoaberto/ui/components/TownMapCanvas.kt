package com.example.mundoaberto.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mundoaberto.data.model.Agent
import com.example.mundoaberto.data.model.TownLocation
import com.example.mundoaberto.ui.theme.*

@Composable
fun TownMapCanvas(
    agents: List<Agent>,
    selectedAgentId: String?,
    selectedLocationId: String?,
    onAgentClick: (String) -> Unit,
    onLocationClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val infiniteTransition = rememberInfiniteTransition(label = "map_anim")
    val pulseAnim by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    val rippleAnim by infiniteTransition.animateFloat(
        initialValue = 10f,
        targetValue = 35f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "fountain_ripple"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(340.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(DarkSurfaceVariant)
            .testTag("town_map_canvas")
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(agents, TownLocation.ALL_LOCATIONS) {
                    detectTapGestures { tapOffset ->
                        val canvasWidth = size.width
                        val canvasHeight = size.height
                        val normX = tapOffset.x / canvasWidth
                        val normY = tapOffset.y / canvasHeight

                        // Check if tapped on an agent first
                        val tappedAgent = agents.firstOrNull { agent ->
                            val ax = agent.position.x
                            val ay = agent.position.y
                            val dist = kotlin.math.sqrt(
                                (normX - ax) * (normX - ax) + (normY - ay) * (normY - ay)
                            )
                            dist < 0.08f
                        }

                        if (tappedAgent != null) {
                            onAgentClick(tappedAgent.id)
                            return@detectTapGestures
                        }

                        // Check if tapped on a location
                        val tappedLocation = TownLocation.ALL_LOCATIONS.minByOrNull { loc ->
                            val lx = loc.center.x
                            val ly = loc.center.y
                            (normX - lx) * (normX - lx) + (normY - ly) * (normY - ly)
                        }

                        if (tappedLocation != null) {
                            val lx = tappedLocation.center.x
                            val ly = tappedLocation.center.y
                            val dist = kotlin.math.sqrt(
                                (normX - lx) * (normX - lx) + (normY - ly) * (normY - ly)
                            )
                            if (dist < 0.22f) {
                                onLocationClick(tappedLocation.id)
                            }
                        }
                    }
                }
        ) {
            val w = size.width
            val h = size.height

            // 1. Draw Map Terrain & Grass Base
            drawRect(
                color = Color(0xFF131B2A),
                size = size
            )

            // 2. Draw Paths / Cobblestones connecting locations
            drawCobblePaths(w, h)

            // 3. Draw Town Locations / Zones
            for (location in TownLocation.ALL_LOCATIONS) {
                val isSelected = location.id == selectedLocationId
                drawTownLocation(
                    location = location,
                    w = w,
                    h = h,
                    isSelected = isSelected,
                    ripple = rippleAnim,
                    textMeasurer = textMeasurer
                )
            }

            // 4. Draw Agents
            for (agent in agents) {
                val isSelected = agent.id == selectedAgentId
                drawAgentAvatar(
                    agent = agent,
                    w = w,
                    h = h,
                    isSelected = isSelected,
                    pulse = if (isSelected) pulseAnim else 1.0f,
                    textMeasurer = textMeasurer
                )
            }
        }
    }
}

private fun DrawScope.drawCobblePaths(w: Float, h: Float) {
    val pathColor = Color(0xFF26334D)
    val pathBorderColor = Color(0xFF1B2438)

    // Center plaza coords
    val cx = 0.50f * w
    val cy = 0.48f * h

    val connections = listOf(
        Offset(0.25f * w, 0.30f * h), // to Cafe
        Offset(0.75f * w, 0.28f * h), // to Library
        Offset(0.78f * w, 0.72f * h), // to Park
        Offset(0.24f * w, 0.70f * h), // to Market
        Offset(0.50f * w, 0.82f * h)  // to Residences
    )

    for (dest in connections) {
        drawLine(
            color = pathBorderColor,
            start = Offset(cx, cy),
            end = dest,
            strokeWidth = 24f
        )
        drawLine(
            color = pathColor,
            start = Offset(cx, cy),
            end = dest,
            strokeWidth = 18f
        )
    }

    // Outer connecting loop between park and residences, market and residences
    drawLine(
        color = pathColor,
        start = Offset(0.24f * w, 0.70f * h),
        end = Offset(0.50f * w, 0.82f * h),
        strokeWidth = 14f
    )
    drawLine(
        color = pathColor,
        start = Offset(0.78f * w, 0.72f * h),
        end = Offset(0.50f * w, 0.82f * h),
        strokeWidth = 14f
    )
}

private fun DrawScope.drawTownLocation(
    location: TownLocation,
    w: Float,
    h: Float,
    isSelected: Boolean,
    ripple: Float,
    textMeasurer: TextMeasurer
) {
    val cx = location.center.x * w
    val cy = location.center.y * h

    when (location.id) {
        "plaza" -> {
            // Plaza paving
            drawCircle(
                color = Color(0xFF1E293B),
                radius = 42f,
                center = Offset(cx, cy)
            )
            // Fountain pool
            drawCircle(
                color = Color(0xFF0284C7),
                radius = 24f,
                center = Offset(cx, cy)
            )
            // Fountain ripple
            drawCircle(
                color = Color(0xFF38BDF8).copy(alpha = (35f - ripple).coerceIn(0f, 25f) / 25f),
                radius = ripple,
                center = Offset(cx, cy),
                style = Stroke(width = 2.5f)
            )
            // Fountain centerpiece
            drawCircle(
                color = Color(0xFFE2E8F0),
                radius = 8f,
                center = Offset(cx, cy)
            )
        }
        "cafe" -> {
            // Cafe terrace
            drawRoundRect(
                color = Color(0xFF134E4A),
                topLeft = Offset(cx - 36f, cy - 28f),
                size = Size(72f, 56f),
                cornerRadius = CornerRadius(12f, 12f)
            )
            drawRoundRect(
                color = Color(0xFF0D9488),
                topLeft = Offset(cx - 32f, cy - 24f),
                size = Size(64f, 48f),
                cornerRadius = CornerRadius(10f, 10f),
                style = Stroke(width = 2f)
            )
        }
        "library" -> {
            // Library stone hall
            drawRoundRect(
                color = Color(0xFF312E81),
                topLeft = Offset(cx - 38f, cy - 28f),
                size = Size(76f, 56f),
                cornerRadius = CornerRadius(10f, 10f)
            )
            // Pillars
            drawLine(
                color = Color(0xFF6366F1),
                start = Offset(cx - 24f, cy - 20f),
                end = Offset(cx - 24f, cy + 20f),
                strokeWidth = 3f
            )
            drawLine(
                color = Color(0xFF6366F1),
                start = Offset(cx + 24f, cy - 20f),
                end = Offset(cx + 24f, cy + 20f),
                strokeWidth = 3f
            )
        }
        "park" -> {
            // Garden / Pond area
            drawCircle(
                color = Color(0xFF064E3B),
                radius = 38f,
                center = Offset(cx, cy)
            )
            // Pond
            drawCircle(
                color = Color(0xFF0891B2),
                radius = 16f,
                center = Offset(cx + 8f, cy + 6f)
            )
            // Little trees
            drawCircle(color = Color(0xFF10B981), radius = 10f, center = Offset(cx - 16f, cy - 14f))
            drawCircle(color = Color(0xFF34D399), radius = 8f, center = Offset(cx + 14f, cy - 16f))
        }
        "market" -> {
            // Market stalls
            drawRoundRect(
                color = Color(0xFF78350F),
                topLeft = Offset(cx - 34f, cy - 28f),
                size = Size(68f, 56f),
                cornerRadius = CornerRadius(10f, 10f)
            )
            // Striped awning
            drawRoundRect(
                color = Color(0xFFF59E0B),
                topLeft = Offset(cx - 30f, cy - 24f),
                size = Size(60f, 20f),
                cornerRadius = CornerRadius(6f, 6f)
            )
        }
        "residences" -> {
            // Cozy cottages
            drawRoundRect(
                color = Color(0xFF1E293B),
                topLeft = Offset(cx - 44f, cy - 24f),
                size = Size(88f, 48f),
                cornerRadius = CornerRadius(12f, 12f)
            )
            drawRoundRect(
                color = Color(0xFF475569),
                topLeft = Offset(cx - 40f, cy - 20f),
                size = Size(80f, 40f),
                cornerRadius = CornerRadius(8f, 8f),
                style = Stroke(width = 2f)
            )
        }
    }

    if (isSelected) {
        drawCircle(
            color = PrimarySky,
            radius = 48f,
            center = Offset(cx, cy),
            style = Stroke(width = 3.5f)
        )
    }

    // Label under location
    val label = location.name.substringBefore("&").substringBefore(" ").take(10)
    val textResult = textMeasurer.measure(
        text = label,
        style = TextStyle(
            color = if (isSelected) PrimarySky else TextSecondary,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    )
    drawText(
        textLayoutResult = textResult,
        topLeft = Offset(cx - (textResult.size.width / 2), cy + 32f)
    )
}

private fun DrawScope.drawAgentAvatar(
    agent: Agent,
    w: Float,
    h: Float,
    isSelected: Boolean,
    pulse: Float,
    textMeasurer: TextMeasurer
) {
    val ax = agent.position.x * w
    val ay = agent.position.y * h
    val color = Color(agent.avatarColor)

    // Selection highlight halo
    if (isSelected) {
        drawCircle(
            color = PrimarySky.copy(alpha = 0.35f),
            radius = 28f * pulse,
            center = Offset(ax, ay)
        )
        drawCircle(
            color = PrimarySky,
            radius = 22f * pulse,
            center = Offset(ax, ay),
            style = Stroke(width = 3f)
        )
    }

    // Avatar base circle
    drawCircle(
        color = color,
        radius = 16f,
        center = Offset(ax, ay)
    )
    drawCircle(
        color = Color.White,
        radius = 16f,
        center = Offset(ax, ay),
        style = Stroke(width = 2f)
    )

    // Small initial letter inside avatar
    val initial = agent.name.first().toString()
    val initialResult = textMeasurer.measure(
        text = initial,
        style = TextStyle(
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    )
    drawText(
        textLayoutResult = initialResult,
        topLeft = Offset(ax - (initialResult.size.width / 2), ay - (initialResult.size.height / 2))
    )

    // Agent name pill below avatar
    val firstName = agent.name.substringBefore(" ")
    val nameResult = textMeasurer.measure(
        text = firstName,
        style = TextStyle(
            color = if (isSelected) PrimarySky else TextPrimary,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
    )

    // Mini background capsule for name
    drawRoundRect(
        color = Color(0xDD090D16),
        topLeft = Offset(ax - (nameResult.size.width / 2) - 6f, ay + 18f),
        size = Size(nameResult.size.width + 12f, nameResult.size.height + 4f),
        cornerRadius = CornerRadius(6f, 6f)
    )

    drawText(
        textLayoutResult = nameResult,
        topLeft = Offset(ax - (nameResult.size.width / 2), ay + 20f)
    )

    // Speech / Status bubble indicator if talking or active
    if (agent.isTalking) {
        drawCircle(
            color = AccentRose,
            radius = 6f,
            center = Offset(ax + 12f, ay - 12f)
        )
    }
}
