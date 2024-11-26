package com.example.divisas

class CurrencyConverter(
    baseExchangeRates: Map<Pair<String, String>, Double>,
    private val currencySymbols: Map<String, String>
) {
    private val exchangeRates: MutableMap<Pair<String, String>, Double> = mutableMapOf()

    init {
        // Añadimos las tasas base y sus inversas
        for ((pair, rate) in baseExchangeRates) {
            exchangeRates[pair] = rate
            exchangeRates[Pair(pair.second, pair.first)] = 1 / rate
        }

        // Generar todas las combinaciones posibles entre las monedas
        val currencies = currencySymbols.keys
        for (origin in currencies) {
            for (destination in currencies) {
                if (origin != destination && Pair(origin, destination) !in exchangeRates) {
                    // Calcular la tasa indirecta
                    val rateToUSD = exchangeRates[Pair(origin, "USD")]
                    val rateFromUSD = exchangeRates[Pair("USD", destination)]
                    if (rateToUSD != null && rateFromUSD != null) {
                        exchangeRates[Pair(origin, destination)] = rateToUSD * rateFromUSD
                    }
                }
            }
        }
    }

    fun convert(amount: Double, originCurrency: String, destinationCurrency: String): String {
        if (originCurrency == destinationCurrency) {
            val symbol = currencySymbols[originCurrency] ?: ""
            return "$symbol${String.format("%.2f", amount)}"
        }

        val rate = exchangeRates[Pair(originCurrency, destinationCurrency)]
            ?: throw IllegalArgumentException("No se encontró tasa de cambio para esta conversión.")

        val result = amount * rate
        val symbol = currencySymbols[destinationCurrency] ?: ""
        return "$symbol${String.format("%.2f", result)}"
    }
}

