package com.example.claculator

data class CalculatorState(
    val expression: String = "",
    val result: String = "",
    val error: String? = null
)

fun formatResult(value: Double): String {
    return if (value % 1.0 == 0.0) value.toLong().toString() else value.toString()
}

