package com.example.basicscodelab

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.text.contains
import kotlin.text.indexOf
import kotlin.text.replace
import kotlin.text.replaceFirst
import kotlin.text.replaceRange
import kotlin.text.substring

class CalculatorViewModel : ViewModel() {
    private val _screenValue = MutableStateFlow("")
    val screenValue: StateFlow<String> = _screenValue

    private val _screenResultValue = MutableStateFlow("")
    val screenResultValue: StateFlow<String> = _screenResultValue

    fun updateScreenValue(newValue: String) {
        _screenValue.value += newValue
    }

    fun clear() {
        _screenValue.value = ""
        _screenResultValue.value = ""
    }

    fun delete() {
        _screenValue.value.dropLast(1)
    }

    fun calculateResult() {
        var expressionForCalculation = _screenValue.value

        val piValue = Math.PI.toString()
        expressionForCalculation = expressionForCalculation.replace(Regex("(\\d)π"), "$1*$piValue")
        expressionForCalculation = expressionForCalculation.replace(Regex("π(\\d)"), "$piValue*$1")
        expressionForCalculation = expressionForCalculation.replace("π", piValue)

        val trigRegex = Regex("(sin|cos|tan)\\(([^()]*)\\)")
        while (true) {
            val funcMatch = Regex("(sin|cos|tan)\\(").find(expressionForCalculation)
            if (funcMatch == null) {
                // Check for unmatched opening parentheses
                if (Regex("(sin|cos|tan)\\(").containsMatchIn(expressionForCalculation)) {
                    _screenResultValue.value = "Error: trigonometry requires \")\""
                }
                break
            }

            val func = funcMatch.groupValues[1]
            val funcStartIndex = funcMatch.range.first
            val innerStart = funcMatch.range.last + 1 // position right after '('

            // Use findMatchingParenthesis to handle nested expressions
            val innerEnd = findMatchingParenthesis(expressionForCalculation, innerStart - 1)
            if (innerEnd == -1) {
                _screenResultValue.value = "Error: trigonometry requires \")\""
                return
            }

            val innerExpr = expressionForCalculation.substring(innerStart, innerEnd).trim()

            // Evaluate the inner expression (can include other scientific functions)
            val innerValueStr = evaluateBasicExpression(innerExpr) { err ->
                _screenResultValue.value = err
                null
            }

            val innerValue = innerValueStr.toDoubleOrNull() ?: run {
                _screenResultValue.value = "Error: invalid number inside $func()"
                return
            }

            val radians = Math.toRadians(innerValue as Double)

            val result = when (func) {
                "sin" -> kotlin.math.sin(radians)
                "cos" -> kotlin.math.cos(radians)
                "tan" -> kotlin.math.tan(radians)
                else -> Double.NaN
            }

            val formattedResult = String.format("%.10f", result).trimEnd('0').trimEnd('.')

            expressionForCalculation = expressionForCalculation.replaceRange(funcStartIndex, innerEnd + 1, formattedResult)                }
        while (expressionForCalculation.contains("log(")) {
            val logStart = expressionForCalculation.indexOf("log(")
            val logBodyStart = logStart + 4

            val parenthesisEnd = findMatchingParenthesis(expressionForCalculation, logBodyStart - 1)
            if (parenthesisEnd == -1) {
                _screenResultValue.value = "Error: log requires \")\""
                return
            }

            val argString = expressionForCalculation.substring(logBodyStart, parenthesisEnd).trim()

            val argValueStr = evaluateBasicExpression(argString) { err ->
                _screenResultValue.value = err
                null
            }

            if (argValueStr == null) return

            val arg = argValueStr.toDoubleOrNull() ?: run {
                _screenResultValue.value = "Error: log requires a valid numeric argument"
                return
            }

            if (arg <= 0) {
                _screenResultValue.value = "Error: log cannot be zero or negative"
                return
            }

            val logResult = kotlin.math.log(arg, 10.0)
            val formattedResult = String.format("%.4f", logResult)

            val fullLogExpression = expressionForCalculation.substring(logStart, parenthesisEnd + 1)
            expressionForCalculation = expressionForCalculation.replaceFirst(fullLogExpression, formattedResult)
        }

        if (expressionForCalculation.contains("ln(")) {
            val lnStart = expressionForCalculation.indexOf("ln(")
            val lnBodyStart = lnStart + 3

            val parenthesisEnd = expressionForCalculation.indexOf(")", lnBodyStart)
            val argEndIndex = if (parenthesisEnd != -1) parenthesisEnd else expressionForCalculation.length
            
            val argString = expressionForCalculation.substring(lnBodyStart, argEndIndex).trim()
            val arg = argString.toDoubleOrNull()

            if (arg == null) {
                _screenResultValue.value = "Error: ln requires number"
                return
            }
            if (arg < 0) {
                _screenResultValue.value = "Error: ln zero value"
                return
            }
            if (arg < 1) {
                _screenResultValue.value = "Error: ln negative value"
                return
            }

            val lnResult = kotlin.math.ln(arg).let {
                String.format("%.4f", it)
            }

            val fulllnExpressionEnd = argEndIndex + (if (parenthesisEnd != -1) 1 else 0)
            val fulllnExpression = expressionForCalculation.substring(lnStart, fulllnExpressionEnd)

            expressionForCalculation = expressionForCalculation.replace(fulllnExpression, lnResult)
        }

        if (expressionForCalculation.contains("sqrt(")) {
            val sqrtStart = expressionForCalculation.indexOf("sqrt(")
            val sqrtBodyStart = sqrtStart + 5

            val parenthesisEnd = expressionForCalculation.indexOf(")", sqrtBodyStart)
            val argEndIndex = if (parenthesisEnd != -1) parenthesisEnd else expressionForCalculation.length

            val argString = expressionForCalculation.substring(sqrtBodyStart, argEndIndex).trim()
            val arg = argString.toDoubleOrNull()

            if (arg == null) {
                _screenResultValue.value = "Error: sqrt requires number"
                return
            }
            if (arg < 0) {
                _screenResultValue.value = "Error: sqrt negative value"
                return
            }

            val sqrtResult = kotlin.math.sqrt(arg).let {
                String.format("%.4f", it)
            }

            val fullSqrtExpressionEnd = argEndIndex + (if (parenthesisEnd != -1) 1 else 0)
            val fullSqrtExpression = expressionForCalculation.substring(sqrtStart, fullSqrtExpressionEnd)

            expressionForCalculation = expressionForCalculation.replace(fullSqrtExpression, sqrtResult)
        }

        var invIndex = expressionForCalculation.indexOf("Inv(")
        while (invIndex != -1) {
            val invBodyStart = invIndex + 4 // position after "Inv("

            val parenthesisEnd = findMatchingParenthesis(expressionForCalculation, invBodyStart - 1)
            if (parenthesisEnd == -1) {
                _screenResultValue.value = "Error: Unclosed parenthesis for Inv"
                return
            }

            val fullInvExpressionEnd = parenthesisEnd + 1

            if (fullInvExpressionEnd < expressionForCalculation.length &&
                expressionForCalculation[fullInvExpressionEnd].isDigit()
            ) {
                _screenResultValue.value = "Error: Missing operator after closing parenthesis ')'"
                return
            }

            val argString = expressionForCalculation.substring(invBodyStart, parenthesisEnd).trim()
            var argValue: Double? = null

            val trigRegex = Regex("(sin|cos|tan)\\((-?\\d+(?:\\.\\d+)?)\\)")
            val trigMatch = trigRegex.matchEntire(argString)
            if (trigMatch != null) {
                val func = trigMatch.groupValues[1]
                val angle = trigMatch.groupValues[2].toDoubleOrNull() ?: 0.0
                val radians = Math.toRadians(angle)
                argValue = when (func) {
                    "sin" -> kotlin.math.sin(radians)
                    "cos" -> kotlin.math.cos(radians)
                    "tan" -> kotlin.math.tan(radians)
                    else -> 0.0
                }
            } else {
                argValue = argString.toDoubleOrNull()
            }

            if (argValue == null) {
                _screenResultValue.value = "Error: Invalid value inside Inv()"
                return
            }

            val invResult: Double = if (argValue >= -1.0 && argValue <= 1.0) {
                Math.toDegrees(kotlin.math.asin(argValue))
            } else {
                if (argValue == 0.0) {
                    _screenResultValue.value = "Error: Division by zero in Inv"
                    return
                }
                1.0 / argValue
            }

            val formattedResult = String.format("%.4f", invResult)
            val fullInvExpression = expressionForCalculation.substring(invIndex, fullInvExpressionEnd)
            expressionForCalculation = expressionForCalculation.replaceFirst(fullInvExpression, formattedResult)

            // Check for next Inv occurrence
            invIndex = expressionForCalculation.indexOf("Inv(")
        }

        var factorialRegex = Regex("(\\d+)!").find(expressionForCalculation)
        while (factorialRegex != null) {
            val fullMatch = factorialRegex.value
            val numberString = factorialRegex.groups[1]!!.value
            val number = numberString.toIntOrNull()

            if (number == null || number < 0) {
                _screenResultValue.value = "Error: Invalid Factorial"
                return
            }

            val factResult = factorial(number)

            if (factResult == -1L) {
                _screenResultValue.value = "Error: Factorial out of range (>$20!$)"
                return
            }

            expressionForCalculation = expressionForCalculation.replace(fullMatch, factResult.toString().trim(), ignoreCase = false)
            factorialRegex = Regex("(\\d+)!").find(expressionForCalculation) // Find next occurrence
        }

        var reciprocalRegex = Regex("1/\\s*(\\d+(\\.\\d+)?)").find(expressionForCalculation)
        while (reciprocalRegex != null) {
            val fullMatch = reciprocalRegex.value
            val numberString = reciprocalRegex.groups[1]!!.value
            val number = numberString.toDoubleOrNull()

            if (number == null || number == 0.0) {
                _screenResultValue.value = "Error: Division by zero or missing number after 1/"
                return
            }

            val reciprocalResult = 1.0 / number

            // Replace "1/number" with the result
            expressionForCalculation = expressionForCalculation.replace(fullMatch, String.format("%.4f", reciprocalResult), ignoreCase = false)
            reciprocalRegex = Regex("1/\\s*(\\d+(\\.\\d+)?)").find(expressionForCalculation)
        }
        val numberPattern = "(-?\\d+(?:\\.\\d+)?)"                // Handle Power (x^y)
        val powerPattern = Regex("(-?\\d+(?:\\.\\d+)?)\\^(-?\\d+(?:\\.\\d+)?)");
        var powerMatch = powerPattern.find(expressionForCalculation)

        if (powerMatch == null && expressionForCalculation.contains("^")) {
            _screenResultValue.value = "Error: Power requires base and exponent"
            return
        }

        while (powerMatch != null) {
            val fullMatch = powerMatch.value
            val baseString = powerMatch.groups[1]!!.value
            val exponentString = powerMatch.groups[2]!!.value

            val base = baseString.toDoubleOrNull()
            val exponent = exponentString.toDoubleOrNull()

            if (base == null || exponent == null) {
                _screenResultValue.value = "Error: Invalid base or exponent for ^"
                return
            }

            val powerResult = pow(base, exponent)
            val formattedResult = if (powerResult % 1.0 == 0.0) {
                powerResult.toLong().toString()   // no decimal if whole number
            } else {
                String.format("%.4f", powerResult) // keep 4 decimals if not whole
            }

            val substitution = "$formattedResult"
            expressionForCalculation = expressionForCalculation.replaceFirst(fullMatch, substitution.trim(), ignoreCase = false)

            // Re-find the next power instance
            powerMatch = powerPattern.find(expressionForCalculation)
        }
        val finalResult = evaluateBasicExpression(expressionForCalculation) { err ->
            _screenResultValue.value = err
        }
        _screenResultValue.value = finalResult
    }
}