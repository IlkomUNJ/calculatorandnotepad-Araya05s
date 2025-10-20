//package com.example.basicscodelab
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavHostController
//import com.example.basicscodelab.CalculatorComponents.DarkLightOrange
//
//object CalculatorScreen {
//    @Composable
//    fun setupLayout(navController: NavHostController) {
//        var screenValue by remember { mutableStateOf("") }
//        var screenResultValue by remember { mutableStateOf("") }
//
//        Column(
//            modifier = Modifier
//                .background(Color(0xFF050505))
//                .padding(12.dp)
//                .fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//            Button(
//                onClick = {
//                    // Navigate to the 'detail_screen' route
//                    navController.navigate("home_screen")
//                },
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF9a031e),
//                    contentColor = Color.White
//                ),
//                contentPadding = PaddingValues(10.dp)
//            ) {
//                Text("Go to Home Screen")
//            }
//            Spacer(modifier = Modifier.height(4.dp))
//            CalculatorComponents.calculatorShowMath(modifier = Modifier.padding(2.dp, 2.dp), screenValue)
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(2.dp)
//                    .background(
//                        brush = Brush.horizontalGradient(
//                            colors = listOf(
//                                Color.Transparent,
//                                Color(0xFF9a031e),
//                                Color.Transparent
//                            )
//                        )
//                    )
//            )
//            CalculatorComponents.calculatorShowResult(modifier = Modifier.padding(20.dp, 20.dp), screenResultValue)
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                CalculatorComponents.calculatorScientificButton(Modifier.padding(2.dp), "Sin", screenValue) { updatedValue -> screenValue = updatedValue}
//                CalculatorComponents.calculatorScientificButton(Modifier.padding(2.dp), "Cos", screenValue) { updatedValue -> screenValue = updatedValue}
//                CalculatorComponents.calculatorScientificButton(Modifier.padding(2.dp), "Tan", screenValue) { updatedValue -> screenValue = updatedValue}
//                CalculatorComponents.calculatorScientificButton(Modifier.padding(2.dp), "(", screenValue) { updatedValue -> screenValue = updatedValue}
//            }
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                CalculatorComponents.calculatorScientificButton(Modifier.padding(2.dp), "log", screenValue) { updatedValue -> screenValue = updatedValue}
//                CalculatorComponents.calculatorScientificButton(Modifier.padding(2.dp), "ln", screenValue) { updatedValue -> screenValue = updatedValue}
//                CalculatorComponents.calculatorScientificButton(Modifier.padding(2.dp), "x!", screenValue) { updatedValue -> screenValue = updatedValue}
//                CalculatorComponents.calculatorScientificButton(Modifier.padding(2.dp), ")", screenValue) { updatedValue -> screenValue = updatedValue }
//                // Used the remaining parenthesis here to keep the row at 4
//            }
//
//// --- Scientific Row 3 (Reciprocal & Root/Power) ---
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                CalculatorComponents.calculatorScientificButton(Modifier.padding(2.dp), "1/x", screenValue) { updatedValue -> screenValue = updatedValue}
//                CalculatorComponents.calculatorScientificButton(Modifier.padding(2.dp), "sqrt(x)", screenValue) { updatedValue -> screenValue = updatedValue} // Added sqrt(x)
//                CalculatorComponents.calculatorScientificButton(Modifier.padding(2.dp), "x^y", screenValue) { updatedValue -> screenValue = updatedValue} // Changed "xy" to "x^y" for clarity
//                CalculatorComponents.calculatorScientificButton(Modifier.padding(2.dp), "π", screenValue) { updatedValue -> screenValue = updatedValue} // Core operator moved up
//            }
//
//
//// --- Control Row 4 (AC, Del, Inv) ---
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                CalculatorComponents.calculatorACButton(Modifier.padding(2.dp)) {
//                    screenValue=""
//                    screenResultValue=""
//                }
//                CalculatorComponents.calculatorOperationButton(Modifier.padding(2.dp), "Del", screenValue) { updatedValue -> screenValue = updatedValue}
//                CalculatorComponents.calculatorScientificButton(Modifier.padding(2.dp), "Inv", screenValue) { updatedValue -> screenValue = updatedValue}
//                CalculatorComponents.calculatorOperationButton(Modifier.padding(2.dp), "/", screenValue) { updatedValue -> screenValue = updatedValue}
//            }
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                CalculatorComponents.calculatorButton(Modifier.padding(2.dp), 7, screenValue) { updatedValue -> screenValue = updatedValue}
//                CalculatorComponents.calculatorButton(Modifier.padding(2.dp), 8, screenValue) { updatedValue -> screenValue = updatedValue}
//                CalculatorComponents.calculatorButton(Modifier.padding(2.dp), 9, screenValue) { updatedValue -> screenValue = updatedValue}
//                CalculatorComponents.calculatorOperationButton(Modifier.padding(2.dp), "*", screenValue) { updatedValue -> screenValue = updatedValue}
//            }
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                CalculatorComponents.calculatorButton(Modifier.padding(2.dp), 4, screenValue) { updatedValue -> screenValue = updatedValue}
//                CalculatorComponents.calculatorButton(Modifier.padding(2.dp), 5, screenValue) { updatedValue -> screenValue = updatedValue}
//                CalculatorComponents.calculatorButton(Modifier.padding(2.dp), 6, screenValue) { updatedValue -> screenValue = updatedValue}
//                CalculatorComponents.calculatorOperationButton(Modifier.padding(2.dp), "+", screenValue) { updatedValue -> screenValue = updatedValue}
//
//            }
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                CalculatorComponents.calculatorButton(Modifier.padding(2.dp), 1, screenValue) { updatedValue -> screenValue = updatedValue}
//                CalculatorComponents.calculatorButton(Modifier.padding(2.dp), 2, screenValue) { updatedValue -> screenValue = updatedValue}
//                CalculatorComponents.calculatorButton(Modifier.padding(2.dp), 3, screenValue) { updatedValue -> screenValue = updatedValue}
//                CalculatorComponents.calculatorOperationButton(Modifier.padding(2.dp), "-", screenValue) { updatedValue -> screenValue = updatedValue}
//
//            }
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                CalculatorComponents.calculatorDoubleButton(Modifier.padding(2.dp), "000", screenValue) { updatedValue -> screenValue = updatedValue}
//                CalculatorComponents.calculatorButton(Modifier.padding(2.dp), 0, screenValue) { updatedValue -> screenValue = updatedValue}
//                CalculatorComponents.showCalculatorResult(Modifier.padding(2.dp), screenValue = screenValue) { updatedValue -> screenResultValue = updatedValue }
//            }
//        }
//    }
//}
////    val LightOrange = Color(0xFFF7A5A5)
////    val DarkLightOrange = Color(0xFF9a031e)
////
////    @Composable
////    fun calculatorButton(modifier: Modifier, value : Int, currentValue: String, onValueAppended: (String) -> Unit) {
////        var buttonText = remember { mutableStateOf("$value") }
////        ElevatedButton(
////            onClick = {
////                val newValue = currentValue + value.toString()
////                onValueAppended(newValue)
////            },
////            colors = ButtonDefaults.buttonColors(
////                containerColor = DarkLightOrange,
////                contentColor = Color.White
////            ), modifier = modifier
////                .widthIn(min = 40.dp)
////                .size(90.dp, 80.dp)
////                .padding(horizontal = 1.dp),
////            shape = RoundedCornerShape(4.dp)
////        ) {
////            Text(buttonText.value, fontSize = 24.sp, color = Color.White)
////        }
////    }
////
////    @Composable
////    fun calculatorACButton(modifier: Modifier, OnAC: () -> Unit) {
////        ElevatedButton(
////            onClick = {OnAC()},
////            colors = ButtonDefaults.buttonColors(
////                containerColor = LightOrange,
////                contentColor = Color.Black
////            ), modifier = modifier
////                .widthIn(min = 40.dp)
////                .size(90.dp, 80.dp)
////                .padding(horizontal = 2.dp),
////            shape = RoundedCornerShape(4.dp)
////        ) {
////            Text(text = "AC", fontSize = 20.sp, color = Color.Black)
////        }
////    }
////    @Composable
////    fun showCalculatorResult(modifier: Modifier, screenValue: String, onValueUpdated: (String) -> Unit) {
////        ElevatedButton(
////            onClick = {
////                val operators = listOf("/", "*", "+", "-")
////
////                var expressionForCalculation = screenValue
////
////                var factorialRegex = Regex("(\\d+)!").find(expressionForCalculation)
////                while (factorialRegex != null) {
////                    val fullMatch = factorialRegex.value
////                    val numberString = factorialRegex.groups[1]!!.value
////                    val number = numberString.toIntOrNull()
////
////                    if (number == null || number < 0) {
////                        onValueUpdated("Error: Invalid Factorial")
////                        return@ElevatedButton
////                    }
////
////                    val factResult = factorial(number)
////
////                    if (factResult == -1L) {
////                        onValueUpdated("Error: Factorial out of range (>$20!$)")
////                        return@ElevatedButton
////                    }
////
////                    // Use .trim() to ensure a clean substitution, preventing hidden whitespace errors
////                    expressionForCalculation = expressionForCalculation.replace(fullMatch, factResult.toString().trim(), ignoreCase = false)
////                    factorialRegex = Regex("(\\d+)!").find(expressionForCalculation) // Find next occurrence
////                }
////
////                // Handle Reciprocal (1/x)
////                // Matches '1/' followed by a number (including decimals), allowing spaces
////                var reciprocalRegex = Regex("1/\\s*(\\d+(\\.\\d+)?)").find(expressionForCalculation)
////                while (reciprocalRegex != null) {
////                    val fullMatch = reciprocalRegex.value
////                    val numberString = reciprocalRegex.groups[1]!!.value
////                    val number = numberString.toDoubleOrNull()
////
////                    if (number == null || number == 0.0) {
////                        onValueUpdated("Error: Division by zero or missing number after 1/")
////                        return@ElevatedButton
////                    }
////
////                    val reciprocalResult = 1.0 / number
////
////                    // Replace "1/number" with the result
////                    expressionForCalculation = expressionForCalculation.replace(fullMatch, String.format("%.4f", reciprocalResult), ignoreCase = false)
////                    reciprocalRegex = Regex("1/\\s*(\\d+(\\.\\d+)?)").find(expressionForCalculation)
////                }
////
////
////                // 3. Binary Operation Phase: Sequential Evaluation (mod, Add Alpha, etc.)
////                var result: Double? = null
////                // Strip remaining parenthesis and trim whitespace before binary operations
////                var remainingExpression = expressionForCalculation.replace("(", "").replace(")", "").trim()
////                var currentOperator: String? = null
////
////                // Loop until no more operators are found in the expression.
////                while (true) {
////                    // Find the index of the first operator.
////                    val operatorIndex = operators.map { remainingExpression.indexOf(it) }
////                        .filter { it != -1 }
////                        .minOrNull()
////
////                    if (operatorIndex == null) {
////                        // No more operators, handle the last number or initial state.
////                        val finalNumber = remainingExpression.trim().toDoubleOrNull()
////                        if (result == null) {
////                            result = finalNumber
////                        }
////                        break
////                    }
////                    val operator = operators.first { remainingExpression.substring(operatorIndex).startsWith(it) }
////                    val parts = remainingExpression.split(operator, limit = 2)
////                    val leftOperand = parts[0].trim().toIntOrNull()?.toDouble()
////
////                    if (leftOperand == null) {
////                        onValueUpdated("Error")
////                        return@ElevatedButton
////                    }
////
////                    if (result == null) {
////                        result = leftOperand
////                    }
////
////                    when (currentOperator) {
////                        "/" -> result = result!! / leftOperand
////                        "*" -> result = result!! * leftOperand
////                        "+" -> result = result!! + leftOperand
////                        "-" -> result = result!! - leftOperand
////                    }
////
////                    currentOperator = operator
////                    remainingExpression = parts[1].trim()
////
////                }
////                val finalNumber = remainingExpression.toIntOrNull()?.toDouble()
////                if (finalNumber != null && result != null && currentOperator != null) {
////                    when (currentOperator) {
////                        "/" -> result = result!! / finalNumber
////                        "*" -> result = result!! * finalNumber
////                        "+" -> result = result!! + finalNumber
////                        "-" -> result = result!! - finalNumber
////                    }
////                } else if (result == null && finalNumber != null) {
////                    // FIX: If no binary operations were used, the result of the unary operation or the single number is the final result.
////                    result = finalNumber
////                } else if (result == null && finalNumber == null && !screenValue.isBlank()) {
////                    onValueUpdated("Error")
////                    return@ElevatedButton
////                }
////
////                val finalResult = if (result != null) {
////                    if (result.rem(1) == 0.0) result.toLong().toString() else result.toString()
////                } else {
////                    "Error"
////                }
////
////                onValueUpdated(finalResult)
////            },
////            colors = ButtonDefaults.buttonColors(
////                containerColor = LightOrange,
////                contentColor = Color.Black
////            ), modifier = modifier
////                .widthIn(min = 80.dp)
////                .size(180.dp, 80.dp)
////                .padding(horizontal = 3.dp),
////            shape = RoundedCornerShape(4.dp)
////        ) {
////            Text(text = "=", fontSize = 20.sp, color = Color.Black)
////        }
////    }
////    @Composable
////    fun calculatorOperationButton(modifier: Modifier, value : String, currentValue: String, onValueAppended: (String) -> Unit) {
////        var buttonOperationText = remember { mutableStateOf(value) }
////        ElevatedButton(
////            onClick = {
////                val newValue = when (value) {
////                    "Del" -> currentValue.dropLast(1)
////                    "+" -> currentValue + "+"
////                    "-" -> currentValue + "-"
////                    "*" -> currentValue + "*"
////                    "/" -> currentValue + "/"
////                    else -> currentValue
////                }
////
////                onValueAppended(newValue)
////            },
////            colors = ButtonDefaults.buttonColors(
////                containerColor = LightOrange,
////                contentColor = Color.Black
////            ), modifier = modifier
////                .widthIn(min = 40.dp)
////                .size(90.dp, 80.dp)
////                .padding(horizontal = 1.dp),
////            shape = RoundedCornerShape(4.dp)
////        ) {
////            Text(buttonOperationText.value, fontSize = 20.sp, color = Color.Black)
////        }
////    }
////
////    @Composable
////    fun calculatorScientificButton(modifier: Modifier, value : String, currentValue: String, onValueAppended: (String) -> Unit) {
////        var buttonScientificText = remember { mutableStateOf(value) }
////        ElevatedButton(
////            onClick = {
////                val newValue = when (value) {
////                    "(" -> currentValue + "("
////                    ")" -> currentValue + ")"
////                    "1/x" -> currentValue + "1/"
////                    "x!" -> currentValue + "!"
////                    else -> currentValue
////                }
////
////                onValueAppended(newValue)
////            },
////            colors = ButtonDefaults.buttonColors(
////                containerColor = LightOrange,
////                contentColor = Color.Black
////            ), modifier = modifier
////                .widthIn(min = 40.dp)
////                .size(90.dp, 80.dp)
////                .padding(horizontal = 1.dp),
////            shape = RoundedCornerShape(4.dp)
////        ) {
////            Text(buttonScientificText.value, fontSize = 20.sp, color = Color.Black)
////        }
////    }
////}
////
//////Helper Function
////fun factorial(n: Int): Long {
////    // Only handles positive integers. Factorial of large numbers will overflow Long.
////    return when {
////        n < 0 -> -1L // Error for negative numbers
////        n > 20 -> -1L // Factorial of 21 and above overflows Long
////        n == 0 || n == 1 -> 1L
////        else -> {
////            var result = 1L
////            for (i in 2..n) {
////                result *= i
////            }
////            result
////        }
////    }
////}
//
////
////@Composable
////fun calculatorShowMath(modifier: Modifier, value: String) {
////    var shouldShowCalculatorResult = rememberSaveable() { mutableStateOf(true) }
////
////    Column(
////        modifier = Modifier
////            .widthIn(5.dp)
////            .padding(vertical = 4.dp)
////    ) {
////        displayCalculatorMath(value)
////    }
////}
////
////@Composable
////fun calculatorShowResult(modifier: Modifier, value: String) {
////    var shouldShowCalculatorResult = rememberSaveable() { mutableStateOf(true) }
////
////    Column(
////        modifier = Modifier
////            .widthIn(5.dp)
////            .padding(vertical = 16.dp)
////    ) {
////        displayCalculatorResult(value)
////    }
////}
////@Composable
////fun displayCalculatorMath(value: String) {
////    Text(
////        text = if (value.isEmpty()) "0" else value,
////        fontSize = 28.sp,
////        color = MaterialTheme.colorScheme.onSurface,
////        modifier = Modifier.padding(16.dp),
////    )
////}
////
////@Composable
////fun displayCalculatorResult(value: String) {
////    Text(
////        text = if (value.isEmpty()) "0" else value,
////        fontSize = 36.sp,
////        fontWeight = FontWeight.Bold,
////        color = MaterialTheme.colorScheme.onSurface,
////        modifier = Modifier.padding(16.dp),
////    )
////}
////
////@Preview
////@Composable
////fun calculatorPreview(){
////    setupLayout()
////}
