package com.example.interactive_mobile

import android.app.AlertDialog
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.interactive_mobile.ui.theme.InteractivemobileTheme


class MainActivity : ComponentActivity() {
    private var pontuacaoTimeA: Int = 0
    private var pontuacaoTimeB: Int = 0
    private lateinit var pTimeA: TextView
    private lateinit var pTimeB: TextView

    private var faltasTimeA: Int = 0
    private var faltasTimeB: Int = 0

    private var cestasTimeA: Int = 0
    private var cestasTimeB: Int = 0

    private lateinit var statsCestasATextView: TextView
    private lateinit var statsFaltasATextView: TextView
    private lateinit var statsCestasBTextView: TextView
    private lateinit var statsFaltasBTextView: TextView

    private lateinit var logTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basquete)

        pTimeA = findViewById(R.id.placarTimeA)
        pTimeB = findViewById(R.id.placarTimeB)
        logTextView = findViewById(R.id.logEventos)
        statsCestasATextView = findViewById(R.id.statsCestasA)
        statsFaltasATextView = findViewById(R.id.statsFaltasA)
        statsCestasBTextView = findViewById(R.id.statsCestasB)
        statsFaltasBTextView = findViewById(R.id.statsFaltasB)

        val bTresPontosTimeA: Button = findViewById(R.id.tresPontosA)
        val aTresPontosTimeB: Button = findViewById(R.id.tresPontosB)

        bTresPontosTimeA.setOnClickListener { adicionarPontos(3, "A") }
        aTresPontosTimeB.setOnClickListener { adicionarPontos(3, "B") }

        val bDoisPontosTimeA: Button = findViewById(R.id.doisPontosA)
        val aDoisPontosTimeB: Button = findViewById(R.id.doisPontosB)

        bDoisPontosTimeA.setOnClickListener { adicionarPontos(2, "A") }
        aDoisPontosTimeB.setOnClickListener { adicionarPontos(2, "B") }

        val bTiroLivreTimeA: Button = findViewById(R.id.tiroLivreA)
        val aTiroLivreTimeB: Button = findViewById(R.id.tiroLivreB)

        bTiroLivreTimeA.setOnClickListener { adicionarPontos(1, "A") }
        aTiroLivreTimeB.setOnClickListener { adicionarPontos(1, "B") }

        val bFaltaA: Button = findViewById(R.id.faltaA)
        val aFaltaB: Button = findViewById(R.id.faltaB)

        bFaltaA.setOnClickListener { adicionarFalta("A") }
        aFaltaB.setOnClickListener { adicionarFalta("B") }

        val reiniciarPartida: Button = findViewById(R.id.reiniciarPartida)
        reiniciarPartida.setOnClickListener { reiniciarPartida() }

        val finalizarPartida: Button = findViewById(R.id.finalizarPartida)
        finalizarPartida.setOnClickListener { mostrarResultadoFinal() }

        reiniciarPartida()
    }

    fun adicionarPontos(pontos: Int, time: String) {
        val nomeTime = if (time == "A") "Time A" else "Time B"
        val textoLog = "\n+${pontos} ponto(s) para o $nomeTime"

        logTextView.append(textoLog)

        if (time == "A") {
            pontuacaoTimeA += pontos
            cestasTimeA++
        } else {
            pontuacaoTimeB += pontos
            cestasTimeB++
        }

        atualizarPlacar(time)
    }

    private fun adicionarFalta(time: String) {
        val nomeTime = if (time == "A") "Time A" else "Time B"
        logTextView.append("\nFalta cometida pelo $nomeTime")

        if (time == "A") {
            faltasTimeA++
        } else {
            faltasTimeB++
        }

        atualizarEstatisticas()
    }

    fun atualizarPlacar(time: String) {
        if (time == "A") {
            pTimeA.setText(pontuacaoTimeA.toString())
        } else {
            pTimeB.setText(pontuacaoTimeB.toString())
        }

        atualizarEstatisticas()
        destacarLider()
    }

    private fun atualizarEstatisticas() {
        statsCestasATextView.text = "Cestas: $cestasTimeA"
        statsFaltasATextView.text = "Faltas: $faltasTimeA"
        statsCestasBTextView.text = "Cestas: $cestasTimeB"
        statsFaltasBTextView.text = "Faltas: $faltasTimeB"
    }

    private fun destacarLider() {
        val timeAName = findViewById<TextView>(R.id.timeA)
        val timeBName = findViewById<TextView>(R.id.timeB)

        // 1. Primeiro, resetamos os nomes para o padrão, sem ícones
        timeAName.text = "Time A"
        timeBName.text = "Time B"

        if (pontuacaoTimeA > pontuacaoTimeB) {
            timeAName.text = "👑 Time A"
            timeAName.setTypeface(null, Typeface.BOLD)
            timeBName.setTypeface(null, Typeface.NORMAL)
        } else if (pontuacaoTimeB > pontuacaoTimeA) {
            timeBName.text = "👑 Time B"
            timeBName.setTypeface(null, Typeface.BOLD)
            timeAName.setTypeface(null, Typeface.NORMAL)
        } else {
            timeAName.setTypeface(null, Typeface.NORMAL)
            timeBName.setTypeface(null, Typeface.NORMAL)
        }
    }

    fun reiniciarPartida() {
        pontuacaoTimeA = 0
        pTimeA.setText(pontuacaoTimeA.toString())
        pontuacaoTimeB = 0
        pTimeB.setText(pontuacaoTimeB.toString())

        cestasTimeA = 0
        cestasTimeB = 0
        faltasTimeA = 0
        faltasTimeB = 0

        Toast.makeText(this, "Placar reiniciado", Toast.LENGTH_SHORT).show()

        logTextView.text = "Partida:"

        atualizarEstatisticas()
        destacarLider()
    }

    private fun mostrarResultadoFinal() {
        var vencedor = "Empate"

        if (pontuacaoTimeA > pontuacaoTimeB) vencedor = "Time A"
        else if (pontuacaoTimeB > pontuacaoTimeA) vencedor = "Time B"

        val mensagemResumo = """
            Vencedor: $vencedor
            
            Placar Final:
            Time A ${pontuacaoTimeA} x ${pontuacaoTimeB} Time B
            
            --- Estatísticas Time A ---
            Cestas: $cestasTimeA
            Faltas: $faltasTimeA
            
            --- Estatísticas Time B ---
            Cestas: $cestasTimeB
            Faltas: $faltasTimeB
        """.trimIndent()

        logTextView.append("\n--- PARTIDA FINALIZADA ---")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Fim de Jogo!")
        builder.setMessage(mensagemResumo)
        builder.setPositiveButton("Jogar Novamente") { _, _ ->
            reiniciarPartida()
        }
        val dialog = builder.create()
        dialog.show()
    }
}