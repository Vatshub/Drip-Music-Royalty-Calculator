package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberKey
import com.example.ui.theme.CyberKeyText
import com.example.ui.theme.CyberPurple

@Composable
fun Keypad(
    onAdd: (String) -> Unit,
    onClear: () -> Unit,
    onBackspace: () -> Unit,
    onCalculate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = 10.dp

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        // Calculate key size based on available width: 4 columns + 3 gaps
        val totalSpacing = spacing * 3
        val keySize: Dp = ((maxWidth - totalSpacing) / 4).coerceAtLeast(48.dp)
        // Tall equals button spans 4 rows: 4 * keySize + 3 * spacing
        val tallEqualsHeight: Dp = keySize * 4 + spacing * 3

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            // Left 3 columns containing numbers, function keys, and zero keys
            Column(
                modifier = Modifier.weight(3f),
                verticalArrangement = Arrangement.spacedBy(spacing)
            ) {
                // Row 1: AC, ⌫, 000
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing)
                ) {
                    FnKey(
                        label = "AC",
                        testTag = "key_ac",
                        keySize = keySize,
                        modifier = Modifier.weight(1f)
                    ) { onClear() }

                    FnKey(
                        label = "⌫",
                        testTag = "key_backspace",
                        keySize = keySize,
                        modifier = Modifier.weight(1f)
                    ) { onBackspace() }

                    FnKey(
                        label = "000",
                        testTag = "key_000",
                        keySize = keySize,
                        modifier = Modifier.weight(1f)
                    ) { onAdd("000") }
                }

                // Row 2: 7, 8, 9
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing)
                ) {
                    NumKey(
                        label = "7",
                        testTag = "key_7",
                        keySize = keySize,
                        modifier = Modifier.weight(1f)
                    ) { onAdd("7") }

                    NumKey(
                        label = "8",
                        testTag = "key_8",
                        keySize = keySize,
                        modifier = Modifier.weight(1f)
                    ) { onAdd("8") }

                    NumKey(
                        label = "9",
                        testTag = "key_9",
                        keySize = keySize,
                        modifier = Modifier.weight(1f)
                    ) { onAdd("9") }
                }

                // Row 3: 4, 5, 6
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing)
                ) {
                    NumKey(
                        label = "4",
                        testTag = "key_4",
                        keySize = keySize,
                        modifier = Modifier.weight(1f)
                    ) { onAdd("4") }

                    NumKey(
                        label = "5",
                        testTag = "key_5",
                        keySize = keySize,
                        modifier = Modifier.weight(1f)
                    ) { onAdd("5") }

                    NumKey(
                        label = "6",
                        testTag = "key_6",
                        keySize = keySize,
                        modifier = Modifier.weight(1f)
                    ) { onAdd("6") }
                }

                // Row 4: 1, 2, 3
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing)
                ) {
                    NumKey(
                        label = "1",
                        testTag = "key_1",
                        keySize = keySize,
                        modifier = Modifier.weight(1f)
                    ) { onAdd("1") }

                    NumKey(
                        label = "2",
                        testTag = "key_2",
                        keySize = keySize,
                        modifier = Modifier.weight(1f)
                    ) { onAdd("2") }

                    NumKey(
                        label = "3",
                        testTag = "key_3",
                        keySize = keySize,
                        modifier = Modifier.weight(1f)
                    ) { onAdd("3") }
                }

                // Row 5: 0 (wide, span 2), 00 (span 1)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing)
                ) {
                    WideZeroKey(
                        label = "0",
                        testTag = "key_0",
                        height = keySize,
                        modifier = Modifier.weight(2f)
                    ) { onAdd("0") }

                    NumKey(
                        label = "00",
                        testTag = "key_00",
                        keySize = keySize,
                        modifier = Modifier.weight(1f)
                    ) { onAdd("00") }
                }
            }

            // Right column: decimal '.' on top, tall '=' key spanning rows 2..5
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(spacing)
            ) {
                FnKey(
                    label = ".",
                    testTag = "key_decimal",
                    keySize = keySize,
                    modifier = Modifier.fillMaxWidth()
                ) { onAdd(".") }

                // Tall equals key with explicit height calculation
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(tallEqualsHeight)
                        .clip(RoundedCornerShape(999.dp))
                        .background(CyberGreen)
                        .border(1.dp, CyberGreen, RoundedCornerShape(999.dp))
                        .drawBehind {
                            drawCircle(
                                color = CyberGreen.copy(alpha = 0.5f),
                                radius = size.maxDimension,
                                center = Offset(size.width / 2f, size.height / 2f)
                            )
                        }
                        .clickable { onCalculate() }
                        .testTag("key_equals"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "=",
                        style = TextStyle(
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0A0A0A),
                            shadow = Shadow(
                                color = CyberGreen.copy(alpha = 0.85f),
                                blurRadius = 18f
                            )
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun NumKey(
    label: String,
    testTag: String,
    keySize: Dp,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(keySize)
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(CyberKey)
            .border(1.dp, CyberPurple.copy(alpha = 0.45f), CircleShape)
            .drawBehind {
                drawCircle(
                    color = CyberPurple.copy(alpha = 0.15f),
                    radius = size.minDimension / 2f,
                    center = Offset(size.width / 2f, size.height / 2f)
                )
            }
            .clickable { onClick() }
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = if (label.length > 1) 20.sp else 26.sp,
                color = CyberKeyText,
                fontWeight = FontWeight.Normal
            )
        )
    }
}

@Composable
private fun FnKey(
    label: String,
    testTag: String,
    keySize: Dp,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(keySize)
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(CyberKey)
            .border(1.dp, CyberGreen.copy(alpha = 0.45f), CircleShape)
            .drawBehind {
                drawCircle(
                    color = CyberGreen.copy(alpha = 0.15f),
                    radius = size.minDimension / 2f,
                    center = Offset(size.width / 2f, size.height / 2f)
                )
            }
            .clickable { onClick() }
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = CyberGreen,
                shadow = Shadow(
                    color = CyberGreen.copy(alpha = 0.6f),
                    blurRadius = 6f
                )
            )
        )
    }
}

@Composable
private fun WideZeroKey(
    label: String,
    testTag: String,
    height: Dp,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(999.dp))
            .background(CyberKey)
            .border(1.dp, CyberPurple.copy(alpha = 0.45f), RoundedCornerShape(999.dp))
            .clickable { onClick() }
            .padding(start = 24.dp)
            .testTag(testTag),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 26.sp,
                color = CyberKeyText,
                fontWeight = FontWeight.Normal
            )
        )
    }
}
