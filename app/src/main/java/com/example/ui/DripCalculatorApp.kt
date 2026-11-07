package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.COUNTRY_RATES
import com.example.data.Currency
import com.example.data.PLATFORM_FACTORS
import com.example.ui.components.BrandHeader
import com.example.ui.components.CalculatorScreen
import com.example.ui.components.CurrencyToggle
import com.example.ui.components.CyberDropdown
import com.example.ui.components.DisclaimerDialog
import com.example.ui.components.FooterNote
import com.example.ui.components.Keypad
import com.example.ui.theme.CyberBg
import com.example.ui.theme.CyberBody
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.CyberPurple

@Composable
fun DripCalculatorApp(
    viewModel: CalculatorViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val countryOptions = remember {
        listOf("Global (all countries)") + COUNTRY_RATES.keys.sorted()
    }
    val platformOptions = remember {
        PLATFORM_FACTORS.keys.toList()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBg)
            .drawBehind {
                // Background radial gradients matching HTML body
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(CyberPurple.copy(alpha = 0.18f), Color.Transparent),
                        center = Offset(size.width * 0.20f, 0f),
                        radius = size.width * 0.9f
                    )
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(CyberGreen.copy(alpha = 0.10f), Color.Transparent),
                        center = Offset(size.width * 0.90f, size.height),
                        radius = size.width * 0.9f
                    )
                )
            },
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Calculator Card Container matching HTML max-width 380px and border-radius 36px
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 420.dp)
                    .clip(RoundedCornerShape(36.dp))
                    .background(CyberBody)
                    .border(1.dp, CyberPurple.copy(alpha = 0.45f), RoundedCornerShape(36.dp))
                    .drawBehind {
                        // Ambient glow simulation
                        drawRect(
                            color = CyberPurple.copy(alpha = 0.05f)
                        )
                    }
                    .padding(horizontal = 18.dp, vertical = 20.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Brand Header
                    BrandHeader(modifier = Modifier.padding(bottom = 14.dp))

                    // Screen Display (Streams, Rate, Result) with missing field highlights
                    CalculatorScreen(
                        streamsText = uiState.formattedStreams,
                        rateText = uiState.rawRate,
                        activeTarget = uiState.target,
                        isRateVisible = uiState.currency != Currency.USD,
                        rateLabel = uiState.rateLabel,
                        resultText = uiState.resultText,
                        isError = uiState.isError,
                        isStreamsError = uiState.streamsError,
                        isRateError = uiState.rateError,
                        onSelectTarget = { viewModel.setTarget(it) }
                    )

                    // Dropdowns (Country and Platform side by side) with error highlights
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp, bottom = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CyberDropdown(
                            placeholder = "Country",
                            selectedValue = uiState.selectedCountry,
                            options = countryOptions,
                            onSelect = { viewModel.setCountry(it) },
                            testTag = "country_dropdown",
                            isError = uiState.countryError,
                            modifier = Modifier.weight(1f)
                        )

                        CyberDropdown(
                            placeholder = "Platform",
                            selectedValue = uiState.selectedPlatform,
                            options = platformOptions,
                            onSelect = { viewModel.setPlatform(it) },
                            testTag = "platform_dropdown",
                            isError = uiState.platformError,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Currency toggle buttons
                    CurrencyToggle(
                        selectedCurrency = uiState.currency,
                        onCurrencySelected = { viewModel.setCurrency(it) },
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    // Keypad
                    Keypad(
                        onAdd = { viewModel.add(it) },
                        onClear = { viewModel.clear() },
                        onBackspace = { viewModel.backspace() },
                        onCalculate = { viewModel.calculate() }
                    )

                    // Footer note and disclaimer link
                    FooterNote(
                        onOpenDisclaimer = { viewModel.openDisclaimer() }
                    )
                }
            }
        }

        // Disclaimer dialog
        if (uiState.showDisclaimer) {
            DisclaimerDialog(
                onDismiss = { viewModel.dismissDisclaimer() }
            )
        }
    }
}
