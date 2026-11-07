package com.example.data

/**
 * Country royalty rates per 1,000 streams (base USD).
 */
val COUNTRY_RATES = mapOf(
    "India" to 0.3386,
    "Canada" to 3.1863,
    "United States" to 3.7955,
    "Pakistan" to 0.296,
    "United Kingdom" to 5.0261,
    "Australia" to 2.9242,
    "UAE" to 2.7433,
    "Germany" to 3.4999,
    "Bangladesh" to 0.3232,
    "New Zealand" to 2.9247,
    "Italy" to 2.7331,
    "Hong Kong" to 3.9326,
    "Nepal" to 0.0593,
    "France" to 3.3257,
    "Netherlands" to 4.2247,
    "Ireland" to 4.3766,
    "Saudi Arabia" to 2.3349,
    "Malaysia" to 0.95,
    "Singapore" to 3.5179,
    "Spain" to 2.0786,
    "Turkey" to 0.5593,
    "Portugal" to 1.9356
)

/**
 * Platform factors multiplier when country is selected.
 */
val PLATFORM_FACTORS = mapOf(
    "Spotify" to 1.0,
    "Apple Music / iTunes" to 2.342,
    "YouTube" to 1.8838,
    "Amazon Music" to 1.3771,
    "Snapchat" to 1.0418,
    "JioSaavn" to 0.5973,
    "Meta (Instagram/Facebook)" to 0.5725
)

/**
 * Global platform rates per 1,000 streams (USD).
 */
val PLATFORM_GLOBAL_RATES = mapOf(
    "Spotify" to 0.4137,
    "Apple Music / iTunes" to 2.2692,
    "YouTube" to 1.4682,
    "Amazon Music" to 0.7845,
    "Snapchat" to 0.449,
    "JioSaavn" to 0.1476,
    "Meta (Instagram/Facebook)" to 0.1356
)

enum class Currency(val symbol: String, val label: String) {
    USD("$", "USD"),
    INR("₹", "INR"),
    EUR("€", "EUR")
}
