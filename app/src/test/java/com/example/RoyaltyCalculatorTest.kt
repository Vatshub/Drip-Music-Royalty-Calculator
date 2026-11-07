package com.example

import com.example.data.Currency
import com.example.ui.CalculatorViewModel
import com.example.ui.components.InputTarget
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RoyaltyCalculatorTest {

    private lateinit var viewModel: CalculatorViewModel

    @Before
    fun setUp() {
        viewModel = CalculatorViewModel()
    }

    @Test
    fun testInitialState() {
        val state = viewModel.uiState.value
        assertEquals("", state.rawStreams)
        assertEquals("0", state.formattedStreams)
        assertEquals("", state.rawRate)
        assertEquals(InputTarget.STREAMS, state.target)
        assertEquals(Currency.USD, state.currency)
        assertTrue(state.showDisclaimer)
    }

    @Test
    fun testKeypadInputAndFormatting() {
        viewModel.add("1")
        viewModel.add("0")
        viewModel.add("0")
        viewModel.add("0")
        assertEquals("1000", viewModel.uiState.value.rawStreams)
        assertEquals("1,000", viewModel.uiState.value.formattedStreams)

        viewModel.add("000")
        assertEquals("1000000", viewModel.uiState.value.rawStreams)
        assertEquals("1,000,000", viewModel.uiState.value.formattedStreams)

        viewModel.backspace()
        assertEquals("100000", viewModel.uiState.value.rawStreams)

        viewModel.clear()
        assertEquals("", viewModel.uiState.value.rawStreams)
        assertEquals("0", viewModel.uiState.value.formattedStreams)
    }

    @Test
    fun testMissingFieldsValidation() {
        // Nothing provided
        viewModel.calculate()
        assertEquals("Required field missing", viewModel.uiState.value.resultText)
        assertTrue(viewModel.uiState.value.isError)
        assertTrue(viewModel.uiState.value.streamsError)
        assertTrue(viewModel.uiState.value.countryError)
        assertTrue(viewModel.uiState.value.platformError)

        // Only streams provided
        viewModel.add("1")
        viewModel.add("0")
        viewModel.add("0")
        viewModel.add("0")
        viewModel.calculate()
        assertEquals("Required field missing", viewModel.uiState.value.resultText)
        assertTrue(viewModel.uiState.value.isError)
        assertFalse(viewModel.uiState.value.streamsError)
        assertTrue(viewModel.uiState.value.countryError)
        assertTrue(viewModel.uiState.value.platformError)

        // Country provided but platform missing
        viewModel.setCountry("United States")
        viewModel.calculate()
        assertEquals("Required field missing", viewModel.uiState.value.resultText)
        assertTrue(viewModel.uiState.value.isError)
        assertFalse(viewModel.uiState.value.streamsError)
        assertFalse(viewModel.uiState.value.countryError)
        assertTrue(viewModel.uiState.value.platformError)

        // With platform and INR without rate
        viewModel.setPlatform("Spotify")
        viewModel.setCurrency(Currency.INR)
        viewModel.calculate()
        assertEquals("Required field missing", viewModel.uiState.value.resultText)
        assertTrue(viewModel.uiState.value.isError)
        assertTrue(viewModel.uiState.value.rateError)
    }

    @Test
    fun testGlobalCalculationUSD() {
        // 100,000 streams on Global Spotify
        // Formula: (n / 1000) * PR["Spotify"] (0.4137) = 100 * 0.4137 = 41.37 USD
        viewModel.add("1")
        viewModel.add("0")
        viewModel.add("0")
        viewModel.add("000")
        viewModel.setCountry("Global (all countries)")
        viewModel.setPlatform("Spotify")
        viewModel.calculate()

        assertFalse(viewModel.uiState.value.isError)
        assertEquals("≈ $41.37", viewModel.uiState.value.resultText)
    }

    @Test
    fun testCountryCalculationUSD() {
        // 1,000,000 streams in United States on Apple Music / iTunes
        // Formula: (n / 1000) * CR["United States"] (3.7955) * PF["Apple Music / iTunes"] (2.342)
        // 1000 * 3.7955 * 2.342 = 8,889.061 -> 8,889.06
        viewModel.add("1")
        viewModel.add("000")
        viewModel.add("000")
        viewModel.setCountry("United States")
        viewModel.setPlatform("Apple Music / iTunes")
        viewModel.calculate()

        assertFalse(viewModel.uiState.value.isError)
        assertEquals("≈ $8,889.06", viewModel.uiState.value.resultText)
    }

    @Test
    fun testInrCurrencyWithExchangeRate() {
        // Set currency to INR
        viewModel.setCurrency(Currency.INR)
        assertEquals(InputTarget.RATE, viewModel.uiState.value.target)

        // 100,000 streams in India on Spotify
        // CR["India"] = 0.3386, PF["Spotify"] = 1.0 -> 100 * 0.3386 * 1.0 = 33.86 USD
        viewModel.setTarget(InputTarget.STREAMS)
        viewModel.add("1")
        viewModel.add("0")
        viewModel.add("0")
        viewModel.add("000")
        viewModel.setCountry("India")
        viewModel.setPlatform("Spotify")

        // Without rate, calculation should fail
        viewModel.calculate()
        assertEquals("Required field missing", viewModel.uiState.value.resultText)

        // Add exchange rate: 1 USD = 85.50 INR -> 33.86 * 85.5 = 2,895.03 INR
        viewModel.setTarget(InputTarget.RATE)
        viewModel.add("8")
        viewModel.add("5")
        viewModel.add(".")
        viewModel.add("5")
        viewModel.calculate()

        assertFalse(viewModel.uiState.value.isError)
        assertEquals("≈ ₹2,895.03", viewModel.uiState.value.resultText)
    }

    @Test
    fun testEurCurrencyWithExchangeRate() {
        // Set currency to EUR
        viewModel.setCurrency(Currency.EUR)
        // 100,000 streams on Global Spotify -> 41.37 USD
        // Exchange rate: 1 EUR = 1.08 USD -> 41.37 / 1.08 = 38.3055 -> 38.31 EUR
        viewModel.setTarget(InputTarget.STREAMS)
        viewModel.add("1")
        viewModel.add("0")
        viewModel.add("0")
        viewModel.add("000")
        viewModel.setCountry("Global")
        viewModel.setPlatform("Spotify")

        viewModel.setTarget(InputTarget.RATE)
        viewModel.add("1")
        viewModel.add(".")
        viewModel.add("0")
        viewModel.add("8")
        viewModel.calculate()

        assertFalse(viewModel.uiState.value.isError)
        assertEquals("≈ €38.31", viewModel.uiState.value.resultText)
    }
}
