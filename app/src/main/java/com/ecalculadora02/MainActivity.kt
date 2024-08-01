package com.ecalculadora02

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.ecalculadora02.ui.theme.Calculadora02Theme
import kotlin.math.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Calculadora02Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Calculator()
                }
            }
        }
    }
}

// Composable que define la interfaz de usuario de la calculadora
@Composable
fun Calculator() {
    // Estado para manejar la entrada del usuario y el resultado
    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Mostrar la entrada y el resultado en la parte superior
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            // Mostrar la entrada actual del usuario
            Text(
                text = input,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.fillMaxWidth().align(Alignment.End)
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Mostrar el resultado de la operación
            Text(
                text = result,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth().align(Alignment.End)
            )
        }

        // Definir los botones de la calculadora
        Column {
            val buttonValues = listOf(
                listOf("7", "8", "9", "/"),
                listOf("4", "5", "6", "*"),
                listOf("1", "2", "3", "-"),
                listOf("0", ".", "⌫", "+"),
                listOf("sin", "cos", "tan", "=")
            )

            buttonValues.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    row.forEach { value ->
                        Button(
                            onClick = {
                                onButtonClick(value, input,
                                    onInputChange = { newInput -> input = newInput },
                                    onResultChange = { newResult -> result = newResult })
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                        ) {
                            Text(value)
                        }
                    }
                }
            }
        }
    }
}

// Función que maneja el clic en los botones
private fun onButtonClick(value: String, currentInput: String, onInputChange: (String) -> Unit, onResultChange: (String) -> Unit) {
    when (value) {
        "⌫" -> {
            // Eliminar el último carácter de la entrada
            if (currentInput.isNotEmpty()) {
                onInputChange(currentInput.dropLast(1))
            }
        }
        "=" -> {
            // Evaluar la expresión y mostrar el resultado
            try {
                val evaluatedResult = evaluateExpression(currentInput)
                onResultChange(evaluatedResult.toString())
            } catch (e: Exception) {
                onResultChange("Error")
            }
        }
        else -> {
            // Agregar el valor del botón a la entrada
            onInputChange(currentInput + value)
        }
    }
}

// Función que evalúa la expresión matemática
private fun evaluateExpression(expression: String): Double {
    val trimmedExpression = expression.trim()
    return when {
        // Manejar funciones trigonométricas
        trimmedExpression.startsWith("sin") -> {
            val value = trimmedExpression.removePrefix("sin").toDoubleOrNull() ?: 0.0
            sin(Math.toRadians(value))
        }
        trimmedExpression.startsWith("cos") -> {
            val value = trimmedExpression.removePrefix("cos").toDoubleOrNull() ?: 0.0
            cos(Math.toRadians(value))
        }
        trimmedExpression.startsWith("tan") -> {
            val value = trimmedExpression.removePrefix("tan").toDoubleOrNull() ?: 0.0
            tan(Math.toRadians(value))
        }
        else -> {
            // Manejar operaciones aritméticas básicas
            try {
                // Evaluar expresiones aritméticas simples
                val expr = trimmedExpression.replace(",", ".")
                val result = evalBasicExpression(expr)
                result ?: 0.0
            } catch (e: Exception) {
                0.0
            }
        }
    }
}

// Función que evalúa expresiones aritméticas básicas
private fun evalBasicExpression(expression: String): Double? {
    // Esta función realiza la evaluación básica de operaciones aritméticas.
    // Nota: Solo maneja suma, resta, multiplicación y división.
    return try {
        val result = when {
            expression.contains('+') -> {
                val parts = expression.split('+')
                parts[0].toDoubleOrNull()?.plus(parts[1].toDoubleOrNull() ?: 0.0)
            }
            expression.contains('-') -> {
                val parts = expression.split('-')
                parts[0].toDoubleOrNull()?.minus(parts[1].toDoubleOrNull() ?: 0.0)
            }
            expression.contains('*') -> {
                val parts = expression.split('*')
                parts[0].toDoubleOrNull()?.times(parts[1].toDoubleOrNull() ?: 0.0)
            }
            expression.contains('/') -> {
                val parts = expression.split('/')
                val numerator = parts[0].toDoubleOrNull() ?: 0.0
                val denominator = parts[1].toDoubleOrNull() ?: 1.0
                if (denominator != 0.0) numerator / denominator else Double.NaN
            }
            else -> expression.toDoubleOrNull()
        }
        result
    } catch (e: Exception) {
        null
    }
}

// Vista previa de la calculadora para el diseño en Compose
@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    Calculadora02Theme {
        Calculator()
    }
}
