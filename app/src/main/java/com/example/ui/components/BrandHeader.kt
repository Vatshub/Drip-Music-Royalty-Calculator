package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CyberGold
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberPurple

@Composable
fun BrandHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo with neon gold glow effect
        Image(
            painter = painterResource(id = R.drawable.drip_logo),
            contentDescription = "Drip logo",
            modifier = Modifier
                .size(76.dp)
                .drawBehind {
                    drawCircle(
                        color = CyberGold.copy(alpha = 0.28f),
                        radius = size.minDimension * 0.58f,
                        center = Offset(size.width / 2f, size.height / 2f)
                    )
                }
        )

        Column(
            modifier = Modifier.padding(start = 12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Drip Music Royalty Calculator",
                style = TextStyle(
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CyberGreen,
                    shadow = Shadow(
                        color = CyberGreen.copy(alpha = 0.7f),
                        blurRadius = 8f
                    )
                )
            )
            Text(
                text = "Sound of Drip Music Tech Private Limited",
                style = TextStyle(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 0.6.sp,
                    color = CyberPurple,
                    shadow = Shadow(
                        color = CyberPurple.copy(alpha = 0.6f),
                        blurRadius = 6f
                    )
                ),
                modifier = Modifier.padding(top = 3.dp)
            )
        }
    }
}
