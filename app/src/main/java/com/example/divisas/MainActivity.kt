package com.example.divisas

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var spType: Spinner
    private lateinit var spDest: Spinner
    private lateinit var etAmount: EditText
    private lateinit var tvResult: TextView
    private lateinit var btnCalculate: Button

    // Actualización de las tasas de cambio con valores actuales
    private val exchangeRates = mapOf(
        Pair("USD", "MXN") to 20.58,
        Pair("MXN", "USD") to 0.0485,
        Pair("USD", "EUR") to 0.94,
        Pair("EUR", "USD") to 1.06,
        Pair("MXN", "EUR") to 0.048,
        Pair("EUR", "MXN") to 20.83,
        Pair("USD", "GBP") to 0.75,
        Pair("GBP", "USD") to 1.33,
        Pair("USD", "JPY") to 148.9,
        Pair("JPY", "USD") to 0.0067,
        Pair("USD", "PEN") to 3.71,
        Pair("PEN", "USD") to 0.27,
        Pair("USD", "CAD") to 1.36,
        Pair("CAD", "USD") to 0.74,
        // Tasas adicionales según sea necesario
        Pair("MXN", "GBP") to 0.0375,
        Pair("GBP", "MXN") to 26.67,
        Pair("MXN", "JPY") to 2.75,
        Pair("JPY", "MXN") to 0.36,
        Pair("MXN", "PEN") to 0.18,
        Pair("PEN", "MXN") to 5.56,
        Pair("MXN", "CAD") to 0.0625,
        Pair("CAD", "MXN") to 16.0,
        Pair("EUR", "GBP") to 0.80,
        Pair("GBP", "EUR") to 1.25,
        Pair("EUR", "JPY") to 158.0,
        Pair("JPY", "EUR") to 0.0063,
        Pair("EUR", "PEN") to 4.35,
        Pair("PEN", "EUR") to 0.23,
        Pair("EUR", "CAD") to 1.45,
        Pair("CAD", "EUR") to 0.69,
        Pair("GBP", "JPY") to 198.0,
        Pair("JPY", "GBP") to 0.0051,
        Pair("GBP", "PEN") to 5.0,
        Pair("PEN", "GBP") to 0.20,
        Pair("GBP", "CAD") to 1.31,
        Pair("CAD", "GBP") to 0.76,
        Pair("JPY", "PEN") to 0.034,
        Pair("PEN", "JPY") to 29.41,
        Pair("JPY", "CAD") to 0.012,
        Pair("CAD", "JPY") to 83.33
    )

    // Símbolos de monedas
    private val currencySymbols = mapOf(
        "USD" to "$",
        "MXN" to "MX$",
        "EUR" to "€",
        "GBP" to "£",
        "JPY" to "¥",
        "PEN" to "S/",
        "CAD" to "C$"
    )

    private val currencies = listOf("USD", "MXN", "EUR", "GBP", "JPY", "PEN", "CAD") // Lista de monedas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Referencias a las vistas
        spType = findViewById(R.id.sp_type)
        spDest = findViewById(R.id.sp_t)
        etAmount = findViewById(R.id.et_amount)
        tvResult = findViewById(R.id.tv_total)
        btnCalculate = findViewById(R.id.btn_calculate)

        // Configuración de los Spinners
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spType.adapter = adapter
        spDest.adapter = adapter

        // Configuración del botón para calcular
        btnCalculate.setOnClickListener {
            convertCurrency()
        }
    }

    private fun convertCurrency() {
        val amountText = etAmount.text.toString()

        if (amountText.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa un monto", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountText.toDouble()
        val originCurrency = spType.selectedItem.toString()
        val destinationCurrency = spDest.selectedItem.toString()

        // Si las monedas son iguales, no se realiza la conversión
        if (originCurrency == destinationCurrency) {
            val symbol = currencySymbols[destinationCurrency] ?: ""
            tvResult.text = "$symbol${String.format("%.2f", amount)}"
            return
        }

        // Buscar la tasa de cambio directa
        val rate = exchangeRates[Pair(originCurrency, destinationCurrency)]

        // Si no existe una tasa de cambio directa, intentar la conversión indirecta
        if (rate == null) {
            // Si no se encuentra la tasa de cambio directa, intentamos la conversión pasando por USD
            val rateToUSD = exchangeRates[Pair(originCurrency, "USD")]
            val rateFromUSD = exchangeRates[Pair("USD", destinationCurrency)]

            if (rateToUSD != null && rateFromUSD != null) {
                val result = amount * rateToUSD * rateFromUSD
                val symbol = currencySymbols[destinationCurrency] ?: ""
                tvResult.text = "$symbol${String.format("%.2f", result)}"
                return
            } else {
                Toast.makeText(this, "No se encontró tasa de cambio para esta conversión", Toast.LENGTH_SHORT).show()
                return
            }
        }

        // Realizar la conversión directa
        val result = amount * rate
        val symbol = currencySymbols[destinationCurrency] ?: ""
        tvResult.text = "$symbol${String.format("%.2f", result)}"
    }
}









