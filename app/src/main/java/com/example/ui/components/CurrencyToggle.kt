package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Currency
import com.example.ui.theme.CyberKey
import com.example.ui.theme.CyberKeyText
import com.example.ui.theme.CyberPurple

@Composable
fun CurrencyToggle(
    selectedCurrency: Currency,
    onCurrencySelected: (Currency) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Currency.values().forEach { cur ->
            val isSelected = cur == selectedCurrency
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(999.dp))
                    .background(if (isSelected) CyberPurple else CyberKey)
                    .border(
                        width = 1.dp,
                        color = if (isSelected) CyberPurple else CyberPurple.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(999.dp)
                    )
                    .drawBehind {
                        if (isSelected) {
                            drawCircle(
                                color = CyberPurple.copy(alpha = 0.4f),
                                radius = size.maxDimension,
                                center = Offset(size.width / 2f, size.height / 2f)
                            )
                        }
                    }
                    .clickable { onCurrencySelected(cur) }
                    .padding(vertical = 10.dp)
                    .testTag("currency_${cur.name}"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = cur.symbol,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color.White else CyberKeyText,
                        shadow = if (isSelected) Shadow(
                            color = CyberPurple.copy(alpha = 0.85f),
                            blurRadius = 14f
                        ) else null
                    )
                )
            }
        }
    }
}
