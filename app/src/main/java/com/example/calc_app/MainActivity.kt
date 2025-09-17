package com.example.calc_app

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var tvDisplay: TextView
    private var currentInput: String = ""
    private var operand: Double? = null
    private var pendingOp: String? = null

    private lateinit var tvHistory: TextView
    private val historyList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        tvDisplay = findViewById(R.id.txtResultado)
        tvHistory = findViewById(R.id.txtHistorico)

        // Configuração dos botões
        val digits = listOf(
            "0" to R.id.btn0, "1" to R.id.btn1, "2" to R.id.btn2, "3" to R.id.btn3,
            "4" to R.id.btn4, "5" to R.id.btn5, "6" to R.id.btn6, "7" to R.id.btn7,
            "8" to R.id.btn8, "9" to R.id.btn9, "." to R.id.btnPonto
        )
        digits.forEach { (digit, id) -> findViewById<Button>(id).setOnClickListener { appendDigit(digit) } }

        val ops = listOf(
            "+" to R.id.btnSomar, "-" to R.id.btnSubtrair,
            "*" to R.id.btnMultiplicar, "/" to R.id.btnDividir
        )
        ops.forEach { (op, id) -> findViewById<Button>(id).setOnClickListener { onOperator(op) } }

        findViewById<Button>(R.id.btnIgual).setOnClickListener { onEquals() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { clearAll() }
        findViewById<Button>(R.id.btnBackspace).setOnClickListener { backspace() } // MODIFICADO: Listener do backspace reativado
        findViewById<Button>(R.id.btnPorcentagem).setOnClickListener { onPercentage() }
        findViewById<Button>(R.id.btnInverterSinal).setOnClickListener { onToggleSign() }

        updateDisplay()
    }

    private fun appendDigit(d: String) {
        if (d == "." && currentInput.contains(".")) return
        currentInput = if (currentInput == "0" && d != ".") d else currentInput + d
        updateDisplay()
    }

    private fun onOperator(op: String) {
        if (currentInput.isNotEmpty()) {
            onEquals(isChainingOperation = true)
        }
        pendingOp = op
        operand = currentInput.toDoubleOrNull()
        currentInput = ""
        updateDisplay()
    }

    private fun performOperation(op1: Double, op2: Double, op: String?): Double {
        // ... (lógica da performOperation não mudou)
        return when (op) {
            "+" -> op1 + op2; "-" -> op1 - op2; "*" -> op1 * op2
            "/" -> if (op2 == 0.0) {
                Toast.makeText(this, "Divisão por zero.", Toast.LENGTH_SHORT).show(); op1
            } else op1 / op2
            else -> op2
        }
    }

    private fun onEquals(isChainingOperation: Boolean = false) {
        if (operand != null && pendingOp != null && currentInput.isNotEmpty()) {
            val secondOperand = currentInput.toDoubleOrNull() ?: return
            val result = performOperation(operand!!, secondOperand, pendingOp)
            val resultString = result.toFormattedString()

            val historyEntry = "${operand!!.toFormattedString()} ${pendingOp!!} ${secondOperand.toFormattedString()} = $resultString"
            addToHistory(historyEntry)

            currentInput = resultString
            operand = null
            pendingOp = null

            if (!isChainingOperation) {
                updateDisplay()
            }
        }
    }

    private fun clearAll() {
        currentInput = ""
        operand = null
        pendingOp = null
        historyList.clear()
        updateHistoryDisplay()
        updateDisplay()
    }

    private fun backspace() {
        if (currentInput.isNotEmpty()) {
            currentInput = currentInput.dropLast(1)
            if (currentInput.isEmpty()) {
                currentInput = "0"
            }
            updateDisplay()
        }
    }

    // MODIFICADO: Lógica de porcentagem mais inteligente
    private fun onPercentage() {
        if (currentInput.isNotEmpty()) {
            val value = currentInput.toDouble()
            // Se houver uma operação de soma ou subtração pendente, calcula a % em cima do operando
            if (operand != null && (pendingOp == "+" || pendingOp == "-")) {
                currentInput = (operand!! * (value / 100)).toFormattedString()
            } else { // Senão, apenas converte o número para sua forma percentual (ex: 50 -> 0.5)
                currentInput = (value / 100).toFormattedString()
            }
            updateDisplay()
        }
    }

    private fun onToggleSign() {
        if (currentInput.isNotEmpty() && currentInput != "0") {
            currentInput = if (currentInput.startsWith("-")) currentInput.substring(1) else "-$currentInput"
            updateDisplay()
        }
    }

    private fun addToHistory(entry: String) {
        historyList.add(entry) // MODIFICADO: Adiciona no final da lista
        if (historyList.size > 5) {
            historyList.removeAt(0) // Remove o mais antigo
        }
        updateHistoryDisplay()
    }

    // MODIFICADO: Histórico agora mostra mais recentes por último e destaca o último
    private fun updateHistoryDisplay() {
        if (historyList.isEmpty()) {
            tvHistory.text = ""
            return
        }

        val olderEntries = historyList.dropLast(1).joinToString("<br>")
        val latestEntry = "<b>${historyList.last()}</b>"

        val fullHistoryHtml = if (olderEntries.isNotEmpty()) "$olderEntries<br>$latestEntry" else latestEntry

        // Usa Html.fromHtml para renderizar o negrito (<b>)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvHistory.text = Html.fromHtml(fullHistoryHtml, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            tvHistory.text = Html.fromHtml(fullHistoryHtml)
        }
    }

    // MODIFICADO: updateDisplay agora mostra a operação completa
    private fun updateDisplay() {
        val operandText = operand?.toFormattedString() ?: ""
        val operatorText = if (pendingOp != null) " $pendingOp " else ""

        val displayText = "$operandText$operatorText$currentInput"

        tvDisplay.text = if (displayText.isEmpty()) "0" else displayText
    }

    private fun Double.toFormattedString(): String {
        return if (this % 1.0 == 0.0) this.toLong().toString() else this.toString()
    }

    // ... (onSaveInstanceState e onRestoreInstanceState não precisam de mudanças agora)
    override fun onSaveInstanceState(outState: Bundle) { // ...
        super.onSaveInstanceState(outState)
        outState.putString("currentInput", currentInput)
        operand?.let { outState.putDouble("operand", it) }
        outState.putString("pendingOp", pendingOp)
        outState.putStringArrayList("historyList", ArrayList(historyList))
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) { // ...
        super.onRestoreInstanceState(savedInstanceState)
        currentInput = savedInstanceState.getString("currentInput", "")
        if (savedInstanceState.containsKey("operand")) {
            operand = savedInstanceState.getDouble("operand")
        }
        pendingOp = savedInstanceState.getString("pendingOp")
        savedInstanceState.getStringArrayList("historyList")?.let {
            historyList.clear(); historyList.addAll(it); updateHistoryDisplay()
        }
        updateDisplay()
    }
}