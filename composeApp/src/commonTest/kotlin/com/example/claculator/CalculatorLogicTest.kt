package com.example.claculator

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CalculatorLogicTest {
    @Test
    fun testBasicExpression() {
        when (val r = CalculatorLogic.evaluate("1+2*3-4/2")) {
            is CalcResult.Value -> assertEquals(5.0, r.value, "1+2*3-4/2 should be 5.0")
            is CalcResult.Error -> throw AssertionError("Unexpected error: ${r.message}")
        }
    }

    @Test
    fun testDivisionByZero() {
        when (val r = CalculatorLogic.evaluate("10/0")) {
            is CalcResult.Value -> throw AssertionError("Expected division by zero error but got value ${r.value}")
            is CalcResult.Error -> assertTrue(r.message.contains("Division by zero"), "Expected division by zero message")
        }
    }

    @Test
    fun testInvalidExpression() {
        when (val r = CalculatorLogic.evaluate("++")) {
            is CalcResult.Value -> throw AssertionError("Expected parse error but got value ${r.value}")
            is CalcResult.Error -> assertTrue(r.message.isNotBlank())
        }
    }
}

