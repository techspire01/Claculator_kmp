package com.example.claculator

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun App() {
    MaterialTheme {
        CalculatorScreen(viewModel = CalculatorViewModel())
    }
}