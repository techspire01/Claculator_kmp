package com.example.claculator

import kotlin.math.abs

sealed interface CalcResult {
    data class Value(val value: Double) : CalcResult
    data class Error(val message: String) : CalcResult
}

object CalculatorLogic {
    private val operators = mapOf(
        '+' to Operator(1, Associativity.LEFT),
        '-' to Operator(1, Associativity.LEFT),
        '*' to Operator(2, Associativity.LEFT),
        '/' to Operator(2, Associativity.LEFT)
    )

    private data class Operator(val precedence: Int, val associativity: Associativity)
    private enum class Associativity { LEFT, RIGHT }

    fun evaluate(expression: String): CalcResult {
        val tokens = tokenize(expression)
        if (tokens.isEmpty()) return CalcResult.Error("Empty expression")
        val rpn = try { toRpn(tokens) } catch (e: Exception) { return CalcResult.Error("Parse error") }
        return try { evalRpn(rpn) } catch (e: ArithmeticException) { CalcResult.Error("${e.message}") } catch (e: Exception) { CalcResult.Error("Evaluation error") }
    }

    private fun tokenize(expr: String): List<String> {
        val s = expr.replace('×', '*').replace('÷', '/')
        val tokens = mutableListOf<String>()
        var i = 0
        while (i < s.length) {
            val c = s[i]
            when {
                c.isWhitespace() -> i++
                c.isDigit() || c == '.' -> {
                    val start = i
                    i++
                    while (i < s.length && (s[i].isDigit() || s[i] == '.')) i++
                    tokens += s.substring(start, i)
                }
                c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')' -> {
                    // handle unary minus: if '-' and at start or previous token is operator or '('
                    if (c == '-') {
                        val prev = tokens.lastOrNull()
                        if (prev == null || prev in listOf("+", "-", "*", "/", "(")) {
                            // unary minus: attach to number
                            i++
                            // read number after unary
                            if (i < s.length && (s[i].isDigit() || s[i] == '.')) {
                                val start = i
                                i++
                                while (i < s.length && (s[i].isDigit() || s[i] == '.')) i++
                                tokens += "-${s.substring(start, i)}"
                                continue
                            } else {
                                // lone unary minus -> treat as negative zero
                                tokens += "-0"
                                continue
                            }
                        }
                    }
                    tokens += c.toString()
                    i++
                }
                else -> throw IllegalArgumentException("Invalid char: $c")
            }
        }
        return tokens
    }

    private fun toRpn(tokens: List<String>): List<String> {
        val out = mutableListOf<String>()
        val ops = ArrayDeque<String>()
        for (token in tokens) {
            when {
                token.toDoubleOrNull() != null -> out += token
                token in operators.keys.map { it.toString() } -> {
                    val o1 = token[0]
                    while (ops.isNotEmpty() && ops.first() != "(") {
                        val o2 = ops.first()[0]
                        val op1 = operators[o1]!!
                        val op2 = operators[o2] ?: break
                        if ((op1.associativity == Associativity.LEFT && op1.precedence <= op2.precedence) ||
                            (op1.associativity == Associativity.RIGHT && op1.precedence < op2.precedence)
                        ) {
                            out += ops.removeFirst()
                        } else break
                    }
                    ops.addFirst(token)
                }
                token == "(" -> ops.addFirst(token)
                token == ")" -> {
                    while (ops.isNotEmpty() && ops.first() != "(") out += ops.removeFirst()
                    if (ops.isEmpty() || ops.first() != "(") throw IllegalArgumentException("Mismatched parentheses")
                    ops.removeFirst()
                }
                else -> throw IllegalArgumentException("Unknown token: $token")
            }
        }
        while (ops.isNotEmpty()) {
            val op = ops.removeFirst()
            if (op == "(" || op == ")") throw IllegalArgumentException("Mismatched parentheses")
            out += op
        }
        return out
    }

    private fun evalRpn(rpn: List<String>): CalcResult {
        val stack = ArrayDeque<Double>()
        for (token in rpn) {
            val num = token.toDoubleOrNull()
            if (num != null) {
                stack.addFirst(num)
            } else if (token.length == 1 && token[0] in operators.keys) {
                if (stack.size < 2) return CalcResult.Error("Invalid expression")
                val b = stack.removeFirst()
                val a = stack.removeFirst()
                val res = when (token[0]) {
                    '+' -> a + b
                    '-' -> a - b
                    '*' -> a * b
                    '/' -> {
                        if (abs(b) < 1e-12) throw ArithmeticException("Division by zero")
                        a / b
                    }
                    else -> throw IllegalArgumentException("Unknown op")
                }
                stack.addFirst(res)
            } else return CalcResult.Error("Invalid token: $token")
        }
        if (stack.size != 1) return CalcResult.Error("Invalid expression")
        return CalcResult.Value(stack.first())
    }
}

