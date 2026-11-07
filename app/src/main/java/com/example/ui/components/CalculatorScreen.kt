package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberError
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberMuted
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.CyberScreen
import com.example.ui.theme.CyberText

enum class InputTarget {
    STREAMS,
    RATE
}

@Composable
fun CalculatorScreen(
    streamsText: String,
    rateText: String,
    activeTarget: InputTarget,
    isRateVisible: Boolean,
    rateLabel: String,
    resultText: String,
    isError: Boolean,
    isStreamsError: Boolean = false,
    isRateError: Boolean = false,
    onSelectTarget: (InputTarget) -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "scanline")
    val scanOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 60f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scanOffset"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(CyberScreen)
            .border(
                width = 1.dp,
                color = if (isStreamsError || isRateError) CyberError else CyberGreen,
                shape = RoundedCornerShape(22.dp)
            )
            .drawBehind {
                // Inner purple/green glow simulation or subtle error glow
                val glowColor = if (isStreamsError || isRateError) CyberError else CyberPurple
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(glowColor.copy(alpha = 0.22f), Color.Transparent),
                        center = Offset(size.width / 2f, size.height / 2f),
                        radius = size.width
                    )
                )
            }
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .defaultMinSize(minHeight = 132.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        // Scanlines overlay
        Canvas(modifier = Modifier.matchParentSize()) {
            val step = 3f * density
            val lineStroke = 1f * density
            var y = (scanOffset * density) % step - step
            while (y < size.height) {
                if (y >= 0) {
                    drawLine(
                        color = Color.White.copy(alpha = 0.035f),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = lineStroke
                    )
                }
                y += step
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            // Streams Section
            Text(
                text = if (isStreamsError) "Streams (required)" else "Streams",
                style = TextStyle(
                    fontSize = 12.sp,
                    color = if (isStreamsError) CyberError else CyberMuted,
                    fontWeight = if (isStreamsError) FontWeight.SemiBold else FontWeight.Normal,
                    textAlign = TextAlign.End
                ),
                modifier = Modifier.fillMaxWidth()
            )

            val streamsInteraction = remember { MutableInteractionSource() }
            val streamsScrollState = rememberScrollState()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .then(
                        when {
                            isStreamsError -> Modifier
                                .border(1.5.dp, CyberError, RoundedCornerShape(10.dp))
                                .background(CyberError.copy(alpha = 0.15f))
                            activeTarget == InputTarget.STREAMS -> Modifier
                                .border(1.dp, CyberPurple, RoundedCornerShape(10.dp))
                                .background(CyberPurple.copy(alpha = 0.12f))
                            else -> Modifier
                        }
                    )
                    .clickable(
                        interactionSource = streamsInteraction,
                        indication = null
                    ) {
                        onSelectTarget(InputTarget.STREAMS)
                    }
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                    .testTag("streams_display"),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = streamsText,
                    style = TextStyle(
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Light,
                        color = if (isStreamsError) CyberError else Color.White,
                        textAlign = TextAlign.End,
                        shadow = Shadow(
                            color = if (isStreamsError) CyberError.copy(alpha = 0.6f) else CyberGreen.copy(alpha = 0.45f),
                            blurRadius = 10f
                        )
                    ),
                    maxLines = 1,
                    modifier = Modifier.horizontalScroll(streamsScrollState, reverseScrolling = true)
                )
            }

            // Rate Section (if visible)
            if (isRateVisible) {
                Text(
                    text = rateLabel,
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = if (isRateError) CyberError else CyberMuted,
                        fontWeight = if (isRateError) FontWeight.SemiBold else FontWeight.Normal,
                        textAlign = TextAlign.End
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                )

                val rateInteraction = remember { MutableInteractionSource() }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .then(
                            when {
                                isRateError -> Modifier
                                    .border(1.5.dp, CyberError, RoundedCornerShape(10.dp))
                                    .background(CyberError.copy(alpha = 0.15f))
                                activeTarget == InputTarget.RATE -> Modifier
                                    .border(1.dp, CyberPurple, RoundedCornerShape(10.dp))
                                    .background(CyberPurple.copy(alpha = 0.12f))
                                else -> Modifier
                            }
                        )
                        .clickable(
                            interactionSource = rateInteraction,
                            indication = null
                        ) {
                            onSelectTarget(InputTarget.RATE)
                        }
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                        .testTag("rate_display"),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = if (rateText.isEmpty()) "—" else rateText,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Light,
                            color = if (isRateError) CyberError else CyberText,
                            textAlign = TextAlign.End,
                            shadow = Shadow(
                                color = if (isRateError) CyberError.copy(alpha = 0.6f) else CyberPurple.copy(alpha = 0.45f),
                                blurRadius = 8f
                            )
                        ),
                        maxLines = 1
                    )
                }
            }

            // Result / Error line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .defaultMinSize(minHeight = 22.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                if (resultText.isNotEmpty()) {
                    Text(
                        text = resultText,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = if (isError) FontWeight.Medium else FontWeight.SemiBold,
                            color = if (isError) CyberError else CyberGreen,
                            textAlign = TextAlign.End,
                            shadow = Shadow(
                                color = if (isError) CyberError.copy(alpha = 0.6f) else CyberGreen.copy(alpha = 0.8f),
                                blurRadius = 10f
                            )
                        ),
                        modifier = Modifier.testTag("result_text")
                    )
                }
            }
        }
    }
}
