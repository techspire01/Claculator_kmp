package com.example.claculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalculatorScreen(viewModel: CalculatorViewModel) {
    val state by viewModel.state.collectAsState()

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(12.dp)
        ) {
            // Display
            Column(modifier = Modifier.fillMaxWidth().weight(1f), horizontalAlignment = Alignment.End) {
                Text(
                    text = state.expression.ifEmpty { "0" },
                    fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(8.dp))
                if (state.error != null) {
                    Text(text = state.error ?: "", color = MaterialTheme.colorScheme.error, fontSize = 20.sp)
                } else {
                    Text(text = state.result.ifEmpty { "" }, fontSize = 36.sp, color = MaterialTheme.colorScheme.onBackground)
                }
            }

            Spacer(Modifier.height(8.dp))

            val buttonModifier = Modifier
                .weight(1f)
                .height(64.dp)

            // Buttons grid
            Column(Modifier.fillMaxWidth()) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(modifier = buttonModifier, onClick = { viewModel.onClear() }) { Text("C") }
                    Button(modifier = buttonModifier, onClick = { viewModel.onOperator('/') }) { Text("÷") }
                    Button(modifier = buttonModifier, onClick = { viewModel.onOperator('*') }) { Text("×") }
                    Button(modifier = buttonModifier, onClick = { viewModel.onOperator('-') }) { Text("-") }
                }
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(modifier = buttonModifier, onClick = { viewModel.onDigit('7') }) { Text("7") }
                    Button(modifier = buttonModifier, onClick = { viewModel.onDigit('8') }) { Text("8") }
                    Button(modifier = buttonModifier, onClick = { viewModel.onDigit('9') }) { Text("9") }
                    Button(modifier = buttonModifier, onClick = { viewModel.onOperator('+') }) { Text("+") }
                }
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(modifier = buttonModifier, onClick = { viewModel.onDigit('4') }) { Text("4") }
                    Button(modifier = buttonModifier, onClick = { viewModel.onDigit('5') }) { Text("5") }
                    Button(modifier = buttonModifier, onClick = { viewModel.onDigit('6') }) { Text("6") }
                    Button(modifier = buttonModifier, onClick = { viewModel.onEquals() }) { Text("=") }
                }
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(modifier = buttonModifier, onClick = { viewModel.onDigit('1') }) { Text("1") }
                    Button(modifier = buttonModifier, onClick = { viewModel.onDigit('2') }) { Text("2") }
                    Button(modifier = buttonModifier, onClick = { viewModel.onDigit('3') }) { Text("3") }
                    Button(modifier = buttonModifier, onClick = { viewModel.onDigit('.') }) { Text(".") }
                }
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(modifier = Modifier.weight(2f).height(64.dp), onClick = { viewModel.onDigit('0') }) { Text("0") }
                    Spacer(Modifier.width(8.dp))
                    // empty to keep grid aligned
                    Box(modifier = Modifier.weight(1f))
                    Spacer(Modifier.width(8.dp))
                    Box(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
