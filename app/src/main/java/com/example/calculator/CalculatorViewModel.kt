package com.example.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class CalculatorViewModel : ViewModel() {

    companion object {
        private const val MAX_INPUT_LENGTH = 50
    }

    private val expressionBuilder = StringBuilder()
    private val expressionRegex = Regex("^-?(\\d+[-+/*]?)*\$")
    private val digitRegex = Regex("^-?\\d+$")

    private val _state = MutableLiveData<CalculatorState>()
    val state: LiveData<CalculatorState> = _state

    fun buttonClicked(symbol: Char) {
        if (symbol == Signs.CLEAR) {
            removeLastSymbolFromExpression()
            _state.value = CalculatorState(inputValue = expressionBuilder)
        } else if (symbol == Signs.EQUAL) {
            if (expressionBuilder.isEmpty()) {
                return
            }
            if (!expressionBuilder.last().isDigit()) {
                removeLastSymbolFromExpression()
            }
            val result = try {
                execute(expressionBuilder).toString()
                    .toBigDecimal()
                    .toPlainString()
                    .removeSuffix(".0")
            } catch (e: Exception) {
                e.message!!
            }

            _state.value = CalculatorState(
                inputValue = result,
                expression = "$expressionBuilder="
            )

            expressionBuilder.clear()
        } else {
            val newExpression = "$expressionBuilder$symbol"

            if (newExpression.isValidExpression()) {
                expressionBuilder.append(symbol)
                _state.value = CalculatorState(inputValue = newExpression)
            }
        }
    }

    private fun removeLastSymbolFromExpression() {
        if (expressionBuilder.isNotEmpty()) {
            expressionBuilder.deleteAt(expressionBuilder.lastIndex)
        }
    }

    private fun CharSequence.isValidExpression() =
        expressionRegex.matches(this) && this.length <= MAX_INPUT_LENGTH

    private fun CharSequence.isDigit() = digitRegex.matches(this)

    private fun execute(str: CharSequence): Double {
        if (str.isDigit()) {
            return str.toString().toDouble()
        }

        val operatorIndex = when {
            str.contains(Signs.PLUS) -> str.indexOfLast { it == Signs.PLUS }
            str.contains(Signs.MINUS) && str.indexOf(Signs.MINUS) != 0 -> str.indexOfLast { it == Signs.MINUS }
            else -> str.indexOfFirst { it == Signs.MULTIPLY || it == Signs.DIVISION }
        }

        val leftExpression = str.subSequence(startIndex = 0, endIndex = operatorIndex)
        val rightExpression = str.subSequence(startIndex = operatorIndex + 1, endIndex = str.length)
        val operator: Char = str[operatorIndex]

        return calculate(
            execute(leftExpression), execute(rightExpression), operator
        )
    }

    private fun calculate(a: Double, b: Double, operator: Char): Double {
        return when (operator) {
            Signs.MINUS -> a - b
            Signs.PLUS -> a + b
            Signs.MULTIPLY -> a * b
            Signs.DIVISION -> {
                if (b == 0.0) throw ArithmeticException("Division by zero")
                a / b
            }

            else -> throw Exception("Unknown error")
        }
    }

}