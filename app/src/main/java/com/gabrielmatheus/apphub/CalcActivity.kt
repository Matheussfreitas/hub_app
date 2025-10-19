package com.gabrielmatheus.apphub

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CalcActivity : AppCompatActivity() {
    private lateinit var tvDisplay: TextView
    private var currentInput: String = ""
    private var operand: Double? = null
    private var pendingOp: String? = null

    private lateinit var tvHistory: TextView
    private val historyList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc)

        // INFO: Registra que a tela foi aberta e o ciclo de vida
        LogHelper.i("CalcActivity: onCreate iniciado. Tela da Calculadora aberta.")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tvDisplay = findViewById(R.id.txtResultado)
        tvHistory = findViewById(R.id.txtHistorico)

        // DEBUG: Rastreia a ligação dos componentes, útil para verificar se o layout está correto
        LogHelper.d("CalcActivity: Ligando listeners para botões numéricos e de ponto.")
        val digits = listOf(
            "0" to R.id.btn0, "1" to R.id.btn1, "2" to R.id.btn2, "3" to R.id.btn3,
            "4" to R.id.btn4, "5" to R.id.btn5, "6" to R.id.btn6, "7" to R.id.btn7,
            "8" to R.id.btn8, "9" to R.id.btn9, "." to R.id.btnPonto
        )
        digits.forEach { (digit, id) -> findViewById<Button>(id).setOnClickListener { appendDigit(digit) } }

        LogHelper.d("CalcActivity: Ligando listeners para operadores (+,-,*,/).")
        val ops = listOf(
            "+" to R.id.btnSomar, "-" to R.id.btnSubtrair,
            "*" to R.id.btnMultiplicar, "/" to R.id.btnDividir
        )
        ops.forEach { (op, id) -> findViewById<Button>(id).setOnClickListener { onOperator(op) } }

        findViewById<Button>(R.id.btnIgual).setOnClickListener {
            LogHelper.i("CalcActivity: Botão '=' pressionado.") // INFO
            onEquals()
        }
        findViewById<Button>(R.id.btnClear).setOnClickListener { clearAll() }
        findViewById<Button>(R.id.btnBackspace).setOnClickListener { backspace() }
        findViewById<Button>(R.id.btnPorcentagem).setOnClickListener { onPercentage() }
        findViewById<Button>(R.id.btnInverterSinal).setOnClickListener { onToggleSign() }

        updateDisplay()
        LogHelper.i("CalcActivity: onCreate finalizado.")
    }

    private fun appendDigit(d: String) {
        // VERBOSE: É o nível mais baixo e ideal para eventos repetitivos como pressionar teclas.
        LogHelper.v("appendDigit: Dígito '$d' pressionado. currentInput antes: '$currentInput'")
        if (d == "." && currentInput.contains(".")) return
        currentInput = if (currentInput == "0" && d != ".") d else currentInput + d
        updateDisplay()
        LogHelper.v("appendDigit: currentInput depois: '$currentInput'")
    }

    private fun onOperator(op: String) {
        // DEBUG: Rastreia o estado antes de mudar a lógica, fundamental para debug de calculadora.
        LogHelper.d("onOperator: Operador '$op' pressionado. Estado atual -> currentInput: '$currentInput', operand: $operand, pendingOp: '$pendingOp'")

        if (currentInput.isNotEmpty()) {
            onEquals(isChainingOperation = true)
        }
        pendingOp = op
        operand = currentInput.toDoubleOrNull()
        currentInput = ""
        updateDisplay()
        LogHelper.d("onOperator: Estado após: Novo pendingOp: '$pendingOp', Novo operand: $operand.")
    }

    private fun performOperation(op1: Double, op2: Double, op: String?): Double {
        return when (op) {
            "+" -> op1 + op2; "-" -> op1 - op2; "*" -> op1 * op2
            "/" -> if (op2 == 0.0) {
                // ERROR: Uma falha na lógica de negócio que o usuário vê (Toast).
                Toast.makeText(this, "Divisão por zero.", Toast.LENGTH_SHORT).show()
                LogHelper.e("performOperation: Tentativa de divisão por zero. O resultado será o primeiro operando ($op1).")
                op1
            } else op1 / op2
            else -> op2
        }
    }

    private fun onEquals(isChainingOperation: Boolean = false) {
        if (operand != null && pendingOp != null && currentInput.isNotEmpty()) {
            val secondOperand = currentInput.toDoubleOrNull()
            if (secondOperand == null) {
                // WARNING: Algo deu errado com o input que não era esperado.
                LogHelper.w("onEquals: secondOperand é nulo. Abortando cálculo.")
                return
            }

            // DEBUG: Mostra a operação exata
            LogHelper.d("onEquals: Calculando -> ${operand!!.toFormattedString()} ${pendingOp!!} ${secondOperand.toFormattedString()}")

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
        } else {
            // INFO: O usuário pressionou '=' mas não havia operação pendente.
            LogHelper.i("onEquals: Botão '=' pressionado sem operação pendente.")
        }
    }

    private fun clearAll() {
        // WARNING: Ação destrutiva que limpa todos os dados de estado e histórico.
        LogHelper.w("clearAll: Dados de input, operando e histórico foram limpos.")
        currentInput = ""
        operand = null
        pendingOp = null
        historyList.clear()
        updateHistoryDisplay()
        updateDisplay()
    }

    private fun backspace() {
        LogHelper.v("backspace: Removendo último caractere de '$currentInput'.")
        if (currentInput.isNotEmpty()) {
            currentInput = currentInput.dropLast(1)
            if (currentInput.isEmpty()) {
                currentInput = "0"
            }
            updateDisplay()
        }
    }

    private fun onPercentage() {
        if (currentInput.isNotEmpty()) {
            val value = currentInput.toDouble()

            if (operand != null && (pendingOp == "+" || pendingOp == "-")) {
                currentInput = (operand!! * (value / 100)).toFormattedString()
            } else {
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
        historyList.add(entry)
        if (historyList.size > 5) {
            historyList.removeAt(0)
        }
        updateHistoryDisplay()
    }

    private fun updateHistoryDisplay() {
        if (historyList.isEmpty()) {
            tvHistory.text = ""
            return
        }

        val olderEntries = historyList.dropLast(1).joinToString("<br>")
        val latestEntry = "<b>${historyList.last()}</b>"

        val fullHistoryHtml = if (olderEntries.isNotEmpty()) "$olderEntries<br>$latestEntry" else latestEntry

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvHistory.text = Html.fromHtml(fullHistoryHtml, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            tvHistory.text = Html.fromHtml(fullHistoryHtml)
        }
    }

    private fun updateDisplay() {
        val operandText = operand?.toFormattedString() ?: ""
        val operatorText = if (pendingOp != null) " $pendingOp " else ""

        val displayText = "$operandText$operatorText$currentInput"

        tvDisplay.text = if (displayText.isEmpty()) "0" else displayText
    }

    private fun Double.toFormattedString(): String {
        return if (this % 1.0 == 0.0) this.toLong().toString() else this.toString()
    }

    // INFO: Adicionando logs ao ciclo de vida de persistência
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        LogHelper.i("CalcActivity: onSaveInstanceState. Salvando estado da calculadora.")
        outState.putString("currentInput", currentInput)
        operand?.let { outState.putDouble("operand", it) }
        outState.putString("pendingOp", pendingOp)
        outState.putStringArrayList("historyList", ArrayList(historyList))
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        LogHelper.i("CalcActivity: onRestoreInstanceState. Restaurando estado da calculadora.")
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