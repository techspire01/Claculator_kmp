package com.example.claculator

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CalculatorViewModel {
    private val scope = CoroutineScope(Dispatchers.Default)
    private val _state = MutableStateFlow(CalculatorState())
    val state: StateFlow<CalculatorState> = _state.asStateFlow()

    fun onDigit(d: Char) {
        if (d == '.' && _state.value.expression.takeLastWhile { it != ' ' }.contains('.')) return
        val newExpr = _state.value.expression + d
        _state.value = _state.value.copy(expression = newExpr, error = null)
    }

    fun onOperator(op: Char) {
        if (_state.value.expression.isEmpty()) {
            if (op == '-') onDigit('-')
            return
        }
        val last = _state.value.expression.last()
        if (last in listOf('+', '-', '*', '/', ' ')) return
        val newExpr = _state.value.expression + op
        _state.value = _state.value.copy(expression = newExpr, error = null)
    }

    fun onClear() {
        _state.value = CalculatorState()
    }

    fun onEquals() {
        val expr = _state.value.expression
        if (expr.isBlank()) return
        scope.launch {
            when (val res = CalculatorLogic.evaluate(expr)) {
                is CalcResult.Value -> _state.value = CalculatorState(expression = expr, result = formatResult(res.value), error = null)
                is CalcResult.Error -> _state.value = _state.value.copy(error = res.message)
            }
        }
    }
}

