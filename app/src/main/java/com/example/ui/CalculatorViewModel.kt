package com.example.ui

import androidx.lifecycle.ViewModel
import com.example.data.COUNTRY_RATES
import com.example.data.Currency
import com.example.data.PLATFORM_FACTORS
import com.example.data.PLATFORM_GLOBAL_RATES
import com.example.ui.components.InputTarget
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

data class CalculatorUiState(
    val rawStreams: String = "",
    val rawRate: String = "",
    val target: InputTarget = InputTarget.STREAMS,
    val currency: Currency = Currency.USD,
    val selectedCountry: String = "",
    val selectedPlatform: String = "",
    val resultText: String = "",
    val isError: Boolean = false,
    val streamsError: Boolean = false,
    val countryError: Boolean = false,
    val platformError: Boolean = false,
    val rateError: Boolean = false,
    val showDisclaimer: Boolean = true // Pop-up on launch
) {
    val formattedStreams: String
        get() = if (rawStreams.isEmpty()) "0" else {
            val num = rawStreams.toLongOrNull()
            if (num != null) {
                DecimalFormat("#,###", DecimalFormatSymbols(Locale.US)).format(num)
            } else {
                rawStreams
            }
        }

    val rateLabel: String
        get() = when (currency) {
            Currency.INR -> "Exchange rate (required): 1 USD = ? INR"
            Currency.EUR -> "Exchange rate (required): 1 EUR = ? USD"
            Currency.USD -> ""
        }
}

class CalculatorViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    fun dismissDisclaimer() {
        _uiState.update { it.copy(showDisclaimer = false) }
    }

    fun openDisclaimer() {
        _uiState.update { it.copy(showDisclaimer = true) }
    }

    fun setTarget(target: InputTarget) {
        _uiState.update { it.copy(target = target) }
    }

    fun setCountry(country: String) {
        _uiState.update {
            it.copy(
                selectedCountry = country,
                resultText = "",
                isError = false,
                countryError = false
            )
        }
    }

    fun setPlatform(platform: String) {
        _uiState.update {
            it.copy(
                selectedPlatform = platform,
                resultText = "",
                isError = false,
                platformError = false
            )
        }
    }

    fun setCurrency(currency: Currency) {
        _uiState.update { current ->
            current.copy(
                currency = currency,
                rawRate = "",
                target = if (currency == Currency.USD) InputTarget.STREAMS else InputTarget.RATE,
                resultText = "",
                isError = false,
                rateError = false
            )
        }
    }

    fun add(d: String) {
        _uiState.update { current ->
            var newStreams = current.rawStreams
            var newRate = current.rawRate
            var streamsErr = current.streamsError
            var rateErr = current.rateError

            if (current.target == InputTarget.STREAMS) {
                if (d == ".") return@update current.copy(resultText = "", isError = false)
                newStreams = (newStreams + d).replace(Regex("^0+"), "")
                if (newStreams.length > 12) newStreams = newStreams.substring(0, 12)
                if (newStreams.isNotEmpty()) streamsErr = false
            } else {
                if (d == "." && newRate.contains(".")) return@update current.copy(resultText = "", isError = false)
                if (d == "." && newRate.isEmpty()) newRate = "0"
                newRate = (newRate + d)
                if (newRate.length > 10) newRate = newRate.substring(0, 10)
                val rt = newRate.toDoubleOrNull() ?: 0.0
                if (rt > 0.0) rateErr = false
            }

            current.copy(
                rawStreams = newStreams,
                rawRate = newRate,
                resultText = "",
                isError = false,
                streamsError = streamsErr,
                rateError = rateErr
            )
        }
    }

    fun backspace() {
        _uiState.update { current ->
            if (current.target == InputTarget.STREAMS) {
                current.copy(
                    rawStreams = current.rawStreams.dropLast(1),
                    resultText = "",
                    isError = false
                )
            } else {
                current.copy(
                    rawRate = current.rawRate.dropLast(1),
                    resultText = "",
                    isError = false
                )
            }
        }
    }

    fun clear() {
        _uiState.update { current ->
            if (current.target == InputTarget.STREAMS) {
                current.copy(rawStreams = "", resultText = "", isError = false)
            } else {
                current.copy(rawRate = "", resultText = "", isError = false)
            }
        }
    }

    fun calculate() {
        val state = _uiState.value
        val n = state.rawStreams.toDoubleOrNull() ?: 0.0
        val c = state.selectedCountry
        val p = state.selectedPlatform
        val rt = state.rawRate.toDoubleOrNull() ?: 0.0

        val isStreamsMissing = n <= 0.0
        val isCountryMissing = c.isEmpty()
        val isPlatformMissing = p.isEmpty()
        val isRateMissing = state.currency != Currency.USD && rt <= 0.0

        if (isStreamsMissing || isCountryMissing || isPlatformMissing || isRateMissing) {
            _uiState.update {
                it.copy(
                    resultText = "Required field missing",
                    isError = true,
                    streamsError = isStreamsMissing,
                    countryError = isCountryMissing,
                    platformError = isPlatformMissing,
                    rateError = isRateMissing
                )
            }
            return
        }

        val usd = if (c == "Global (all countries)" || c == "Global") {
            (n / 1000.0) * (PLATFORM_GLOBAL_RATES[p] ?: 0.0)
        } else {
            val countryRate = COUNTRY_RATES[c] ?: 0.0
            val platformFactor = PLATFORM_FACTORS[p] ?: 1.0
            (n / 1000.0) * countryRate * platformFactor
        }

        var v = usd
        var sym = "$"

        when (state.currency) {
            Currency.INR -> {
                v = usd * rt
                sym = "₹"
            }
            Currency.EUR -> {
                v = usd / rt
                sym = "€"
            }
            Currency.USD -> {
                v = usd
                sym = "$"
            }
        }

        val df = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.US))
        val formattedValue = df.format(v)

        _uiState.update {
            it.copy(
                resultText = "≈ $sym$formattedValue",
                isError = false,
                streamsError = false,
                countryError = false,
                platformError = false,
                rateError = false
            )
        }
    }
}
