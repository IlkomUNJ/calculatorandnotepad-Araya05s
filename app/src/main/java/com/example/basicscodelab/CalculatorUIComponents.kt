package com.example.basicscodelab

import android.R.attr.label
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.basicscodelab.CalculatorScreen_Screen.setupLayout
import kotlin.math.PI
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.log
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan
import kotlin.text.toDoubleOrNull

object CalculatorUIComponents {

    @Composable
    fun calculatorShowMath(modifier: Modifier, value: String) {
        var shouldShowCalculatorResult = rememberSaveable() { mutableStateOf(true) }

        Column(
            modifier = Modifier
                .widthIn(5.dp)
                .padding(vertical = 4.dp)
        ) {
            displayCalculatorMath(value)
        }
    }
    @Composable
    fun calculatorShowResult(modifier: Modifier, value: String) {
        Column(
            modifier = Modifier
                .widthIn(5.dp)
                .padding(vertical = 16.dp)
        ) {
            displayCalculatorResult(value)
        }
    }

    val LightOrange = Color(0xFFF7A5A5)
    val DarkLightOrange = Color(0xFF9a031e)

    @Composable
    fun calculatorButton(modifier: Modifier, value : Int, newValue: String, onValueAppended: (String) -> Unit) {
        var buttonText = remember { mutableStateOf("$value") }
        ElevatedButton(
            onClick = {
                val newValue = value.toString()
                onValueAppended(newValue)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkLightOrange,
                contentColor = Color.White
            ), modifier = modifier
                .widthIn(min = 40.dp)
                .size(90.dp, 80.dp)
                .padding(horizontal = 1.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(buttonText.value, fontSize = 24.sp, color = Color.White)
        }
    }
    @Composable
    fun calculatorDoubleButton(modifier: Modifier, value : String, newValue: String, onValueAppended: (String) -> Unit) {
        var buttonText = remember { mutableStateOf("$value") }
        ElevatedButton(
            onClick = {
                val newValue = when (value) {
                    "000" -> "000"
                    else -> newValue
                }

                onValueAppended(newValue)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkLightOrange,
                contentColor = Color.White
            ), modifier = modifier
                .widthIn(min = 40.dp)
                .size(90.dp, 80.dp)
                .padding(horizontal = 1.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(buttonText.value, fontSize = 24.sp, color = Color.White)
        }
    }

    @Composable
    fun calculatorACButton(modifier: Modifier, OnAC: () -> Unit) {
        ElevatedButton(
            onClick = {OnAC()},
            colors = ButtonDefaults.buttonColors(
                containerColor = LightOrange,
                contentColor = Color.Black
            ), modifier = modifier
                .widthIn(min = 40.dp)
                .size(90.dp, 80.dp)
                .padding(horizontal = 2.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "AC", fontSize = 20.sp, color = Color.Black)
        }
    }

    @Composable
    fun showCalculatorResult(modifier: Modifier, screenValue: String, onValueUpdated: (String) -> Unit) {
        ElevatedButton(
            onClick = {
                var expressionForCalculation = screenValue

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
                            onValueUpdated("Error: trigonometry requires \")\"")
                            return@ElevatedButton
                        }
                        break
                    }

                    val func = funcMatch.groupValues[1]
                    val funcStartIndex = funcMatch.range.first
                    val innerStart = funcMatch.range.last + 1 // position right after '('

                    // Use findMatchingParenthesis to handle nested expressions
                    val innerEnd = findMatchingParenthesis(expressionForCalculation, innerStart - 1)
                    if (innerEnd == -1) {
                        onValueUpdated("Error: trigonometry requires \")\"")
                        return@ElevatedButton
                    }

                    val innerExpr = expressionForCalculation.substring(innerStart, innerEnd).trim()

                    // Evaluate the inner expression (can include other scientific functions)
                    val innerValueStr = evaluateBasicExpression(innerExpr) { err ->
                        onValueUpdated(err)
                        null
                    }

                    val innerValue = innerValueStr?.toDoubleOrNull() ?: run {
                        onValueUpdated("Error: invalid number inside $func()")
                        return@ElevatedButton
                    }

                    val radians = Math.toRadians(innerValue)

                    val result = when (func) {
                        "sin" -> sin(radians)
                        "cos" -> cos(radians)
                        "tan" -> tan(radians)
                        else -> Double.NaN
                    }

                    val formattedResult = String.format("%.10f", result).trimEnd('0').trimEnd('.')

                    expressionForCalculation = expressionForCalculation.replaceRange(funcStartIndex, innerEnd + 1, formattedResult)                }
                while (expressionForCalculation.contains("log(")) {
                    val logStart = expressionForCalculation.indexOf("log(")
                    val logBodyStart = logStart + 4

                    val parenthesisEnd = findMatchingParenthesis(expressionForCalculation, logBodyStart - 1)
                    if (parenthesisEnd == -1) {
                        onValueUpdated("Error: log requires \")\"")
                        return@ElevatedButton
                    }

                    val argString = expressionForCalculation.substring(logBodyStart, parenthesisEnd).trim()

                    val argValueStr = evaluateBasicExpression(argString) { err ->
                        onValueUpdated(err)
                        null
                    }

                    if (argValueStr == null) return@ElevatedButton

                    val arg = argValueStr.toDoubleOrNull() ?: run {
                        onValueUpdated("Error: log requires a valid numeric argument")
                        return@ElevatedButton
                    }

                    if (arg <= 0) {
                        onValueUpdated("Error: log cannot be zero or negative")
                        return@ElevatedButton
                    }

                    val logResult = log(arg, 10.0)
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
                        onValueUpdated("Error: ln requires number")
                        return@ElevatedButton
                    }
                    if (arg < 0) {
                        onValueUpdated("Error: ln zero value")
                        return@ElevatedButton
                    }
                    if (arg < 1) {
                        onValueUpdated("Error: ln negative value")
                        return@ElevatedButton
                    }

                    val lnResult = ln(arg).let {
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
                        onValueUpdated("Error: sqrt requires number")
                        return@ElevatedButton
                    }
                    if (arg < 0) {
                        onValueUpdated("Error: sqrt negative value")
                        return@ElevatedButton
                    }

                    val sqrtResult = sqrt(arg).let {
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
                        onValueUpdated("Error: Unclosed parenthesis for Inv")
                        return@ElevatedButton
                    }

                    val fullInvExpressionEnd = parenthesisEnd + 1

                    if (fullInvExpressionEnd < expressionForCalculation.length &&
                        expressionForCalculation[fullInvExpressionEnd].isDigit()
                    ) {
                        onValueUpdated("Error: Missing operator after closing parenthesis ')'")
                        return@ElevatedButton
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
                            "sin" -> sin(radians)
                            "cos" -> cos(radians)
                            "tan" -> tan(radians)
                            else -> 0.0
                        }
                    } else {
                        argValue = argString.toDoubleOrNull()
                    }

                    if (argValue == null) {
                        onValueUpdated("Error: Invalid value inside Inv()")
                        return@ElevatedButton
                    }

                    val invResult: Double = if (argValue >= -1.0 && argValue <= 1.0) {
                        Math.toDegrees(asin(argValue))
                    } else {
                        if (argValue == 0.0) {
                            onValueUpdated("Error: Division by zero in Inv")
                            return@ElevatedButton
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
                        onValueUpdated("Error: Invalid Factorial")
                        return@ElevatedButton
                    }

                    val factResult = factorial(number)

                    if (factResult == -1L) {
                        onValueUpdated("Error: Factorial out of range (>$20!$)")
                        return@ElevatedButton
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
                        onValueUpdated("Error: Division by zero or missing number after 1/")
                        return@ElevatedButton
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
                    onValueUpdated("Error: Power requires base and exponent")
                    return@ElevatedButton
                }

                while (powerMatch != null) {
                    val fullMatch = powerMatch.value
                    val baseString = powerMatch.groups[1]!!.value
                    val exponentString = powerMatch.groups[2]!!.value

                    val base = baseString.toDoubleOrNull()
                    val exponent = exponentString.toDoubleOrNull()

                    if (base == null || exponent == null) {
                        onValueUpdated("Error: Invalid base or exponent for ^")
                        return@ElevatedButton
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
                    onValueUpdated(err)
                }
                onValueUpdated(finalResult)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = LightOrange,
                contentColor = Color.Black
            ), modifier = modifier
                .widthIn(min = 80.dp)
                .size(180.dp, 80.dp)
                .padding(horizontal = 3.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "=", fontSize = 20.sp, color = Color.Black)
        }
    }
    @Composable
    fun calculatorOperationButton(modifier: Modifier, value : String, newValue: String, onValueAppended: (String) -> Unit) {
        var buttonOperationText = remember { mutableStateOf(value) }
        ElevatedButton(
            onClick = {
                val newValue = when (value) {
                    "Del" -> newValue.dropLast(1)
                    "+" -> "+"
                    "-" -> "-"
                    "*" -> "*"
                    "/" -> "/"
                    else -> newValue
                }

                onValueAppended(newValue)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = LightOrange,
                contentColor = Color.Black
            ), modifier = modifier
                .widthIn(min = 40.dp)
                .size(90.dp, 80.dp)
                .padding(horizontal = 1.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(buttonOperationText.value, fontSize = 20.sp, color = Color.Black)
        }
    }

    @Composable
    fun calculatorScientificButton(modifier: Modifier, value : String, newValue: String, onValueAppended: (String) -> Unit) {
        var buttonScientificText = remember { mutableStateOf(value) }
        ElevatedButton(
            onClick = {
                val newValue = when (value) {
                    "(" -> "("
                    ")" -> ")"
                    "log" -> "log("
                    "ln" -> "ln("
                    "x!" -> "!"
                    "Sin" -> "sin("
                    "Cos" -> "cos("
                    "Tan" -> "tan("
                    "Inv" -> "Inv("
                    "1/x" -> "1/"
                    "x^y" -> "^"
                    "sqrt(x)" -> "sqrt("
                    "π" -> "π"

                    else -> newValue
                }

                onValueAppended(newValue)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = LightOrange,
                contentColor = Color.Black
            ), modifier = modifier
                .widthIn(min = 40.dp)
                .size(90.dp, 80.dp)
                .padding(horizontal = 1.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(buttonScientificText.value, fontSize = 20.sp, color = Color.Black)
        }
    }
}

@Composable
fun displayCalculatorMath(value: String) {
    val scrollState = rememberScrollState()

    LaunchedEffect(value) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .horizontalScroll(scrollState),
    ) {
        Text(
            text = if (value.isEmpty()) "0" else value,
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier
                .padding(6.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Start
        )
    }
}
@Composable
fun displayCalculatorResult(value: String) {
    Column(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .heightIn(min = 33.dp * 2)
    ) {
        Text(
            text = if (value.isEmpty()) "0" else value,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            lineHeight = 30.sp,
            modifier = Modifier
                .padding(2.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.End,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

//Helper Function
fun factorial(n: Int): Long {
    return when {
        n < 0 -> -1L // Error for negative numbers
        n > 20 -> -1L // Factorial of 21 and above overflows Long
        n == 0 || n == 1 -> 1L
        else -> {
            var result = 1L
            for (i in 2..n) {
                result *= i
            }
            result
        }
    }
}

fun findMatchingParenthesis(expression: String, startIndex: Int): Int {
    var openCount = 1
    for (i in startIndex + 1 until expression.length) {
        when (expression[i]) {
            '(' -> openCount++
            ')' -> {
                openCount--
                if (openCount == 0) {
                    return i
                }
            }
        }
    }
    return -1
}

fun pow(base: Double, exponent: Double): Double {
    return Math.pow(base, exponent)
}

fun evaluateBasicExpression(
    expression: String,
    onError: (String) -> Unit
): String {

    fun findInnermostParenthesis(expr: String): Pair<Int, Int>? {
        var start = -1
        expr.forEachIndexed { i, c ->
            if (c == '(') start = i
            if (c == ')' && start != -1) return Pair(start, i)
        }
        return null
    }

    var expr = expression.replace(" ", "")

    while (true) {
        val parens = findInnermostParenthesis(expr)
        if (parens == null) break

        val innerExpr = expr.substring(parens.first + 1, parens.second)
        val innerValue = evaluateBasicExpression(innerExpr, onError)
        if (innerValue == "Error") return "Error"

        expr = expr.substring(0, parens.first) + innerValue + expr.substring(parens.second + 1)
    }

    val rawTokens = Regex("\\d+(?:\\.\\d+)?|[+*/-]")
        .findAll(expr)
        .map { it.value }
        .toMutableList()

    if (rawTokens.isEmpty()) {
        return "Error"
    }

    val tokens = mutableListOf<String>()
    var i = 0
    while (i < rawTokens.size) {
        val token = rawTokens[i]
        if (token == "-" && (i == 0 || rawTokens[i - 1] in listOf("+", "-", "*", "/"))) {
            if (i + 1 < rawTokens.size && rawTokens[i + 1].matches(Regex("\\d+(?:\\.\\d+)?"))) {
                tokens.add("-${rawTokens[i + 1]}")
                i += 2
                continue
            } else {
                tokens.add(token)
            }
        } else {
            tokens.add(token)
        }
        i++
    }

    i = 0
    while (i < tokens.size) {
        if (tokens[i] == "*" || tokens[i] == "/") {
            val left = tokens[i - 1].toDoubleOrNull()
            val right = tokens[i + 1].toDoubleOrNull()
            if (left == null || right == null) {
                onError("Error")
                return "Error"
            }
            val intermediate = if (tokens[i] == "*") left * right else left / right
            tokens[i - 1] = intermediate.toString()
            tokens.removeAt(i)
            tokens.removeAt(i)
            i--
        }
        i++
    }

    var result = tokens[0].toDoubleOrNull()
    i = 1
    while (i < tokens.size) {
        val op = tokens[i]
        val right = tokens[i + 1].toDoubleOrNull()
        if (result == null || right == null) {
            onError("Error")
            return "Error"
        }
        result = when (op) {
            "+" -> result + right
            "-" -> result - right
            else -> result
        }
        i += 2
    }

    return if (result != null) {
        if (result.rem(1) == 0.0) result.toLong().toString() else result.toString()
    } else {
        "Error"
    }
}