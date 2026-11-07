package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberMuted

@Composable
fun FooterNote(
    onOpenDisclaimer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val notePrefix = "Estimates only, based on real data from a limited set of countries and platforms. Country and platform combinations are estimated. Actual payouts vary with listener subscription type, platform policies, fees and exchange rates. "
    val linkText = "Full disclaimer"

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = CyberMuted)) {
            append(notePrefix)
        }
        pushStringAnnotation(tag = "DISCLAIMER", annotation = "open")
        withStyle(
            style = SpanStyle(
                color = CyberGreen,
                shadow = Shadow(
                    color = CyberGreen.copy(alpha = 0.5f),
                    blurRadius = 6f
                )
            )
        ) {
            append(linkText)
        }
        pop()
    }

    Text(
        text = annotatedString,
        style = TextStyle(
            fontSize = 11.sp,
            lineHeight = 16.sp,
            textAlign = TextAlign.Center
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 14.dp)
            .clickable { onOpenDisclaimer() }
            .testTag("footer_disclaimer_link")
    )
}
