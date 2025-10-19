package com.gabrielmatheus.apphub

import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PlacarActivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_placar)

        LogHelper.i("PlacarActivity: onCreate iniciado. Tela de placar carregada.")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // DEBUG: Rastreamento da ligação das Views
        LogHelper.d("PlacarActivity: Ligando todas as Views (Placar, Estatísticas e Log).")
        logTextView = findViewById(R.id.logEventos)
        pTimeA = findViewById(R.id.placarTimeA)
        pTimeB = findViewById(R.id.placarTimeB)
        statsCestasATextView = findViewById(R.id.statsCestasA)
        statsFaltasATextView = findViewById(R.id.statsFaltasA)
        statsCestasBTextView = findViewById(R.id.statsCestasB)
        statsFaltasBTextView = findViewById(R.id.statsFaltasB)

        // ... (Ligação dos botões e Listeners)

        val bTresPontosTimeA: Button = findViewById(R.id.tresPontosA)
        val aTresPontosTimeB: Button = findViewById(R.id.tresPontosB)
        bTresPontosTimeA.setOnClickListener {
            LogHelper.v("PlacarActivity: Clique 3 Pontos Time A.") // VERBOSE: Detalhe do clique
            adicionarPontos(3, "A")
        }
        aTresPontosTimeB.setOnClickListener {
            LogHelper.v("PlacarActivity: Clique 3 Pontos Time B.") // VERBOSE
            adicionarPontos(3, "B")
        }

        // ... (Listeners para 2 pontos e Tiro Livre seguem o mesmo padrão VERBOSE) ...
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
        bFaltaA.setOnClickListener {
            LogHelper.v("PlacarActivity: Clique Falta Time A.") // VERBOSE
            adicionarFalta("A")
        }
        aFaltaB.setOnClickListener {
            LogHelper.v("PlacarActivity: Clique Falta Time B.") // VERBOSE
            adicionarFalta("B")
        }

        val reiniciarPartida: Button = findViewById(R.id.reiniciarPartida)
        reiniciarPartida.setOnClickListener {
            LogHelper.w("PlacarActivity: Botão REINICIAR acionado. Dados serão perdidos.") // WARNING: Ação destrutiva
            reiniciarPartida()
        }

        val finalizarPartida: Button = findViewById(R.id.finalizarPartida)
        finalizarPartida.setOnClickListener {
            LogHelper.i("PlacarActivity: Partida FINALIZADA pelo usuário.") // INFO: Evento de fim de ciclo
            mostrarResultadoFinal()
        }

        LogHelper.d("PlacarActivity: Inicializando partida no onCreate.")
        reiniciarPartida()
    }

    fun adicionarPontos(pontos: Int, time: String) {
        val nomeTime = if (time == "A") "Time A" else "Time B"
        // DEBUG: Rastreia a mudança de estado ANTES da alteração
        LogHelper.d("adicionarPontos: Antes - Time $time: Pts $pontuacaoTimeA x $pontuacaoTimeB.")

        val textoLog = "\n+${pontos} ponto(s) para o $nomeTime"
        logTextView.append(textoLog)

        if (time == "A") {
            pontuacaoTimeA += pontos
            cestasTimeA++
        } else {
            pontuacaoTimeB += pontos
            cestasTimeB++
        }

        // DEBUG: Rastreia a mudança de estado DEPOIS da alteração
        LogHelper.d("adicionarPontos: Depois - Time $time: Pts $pontuacaoTimeA x $pontuacaoTimeB. Cesta de $pontos pts.")

        atualizarPlacar(time)
    }

    private fun adicionarFalta(time: String) {
        val nomeTime = if (time == "A") "Time A" else "Time B"
        logTextView.append("\nFalta cometida pelo $nomeTime")

        LogHelper.d("adicionarFalta: Falta para o Time $time. Antes: ${if (time == "A") faltasTimeA else faltasTimeB} faltas.")

        if (time == "A") {
            faltasTimeA++
        } else {
            faltasTimeB++
        }

        LogHelper.d("adicionarFalta: Falta registrada. Depois: ${if (time == "A") faltasTimeA else faltasTimeB} faltas.")

        atualizarEstatisticas()
    }

    fun atualizarPlacar(time: String) {
        if (time == "A") {
            pTimeA.setText(pontuacaoTimeA.toString())
        } else {
            pTimeB.setText(pontuacaoTimeB.toString())
        }
        // DEBUG: Acompanha o fluxo de atualização
        LogHelper.d("atualizarPlacar: Placar do Time $time atualizado na UI.")

        atualizarEstatisticas()
        destacarLider()
    }

    private fun atualizarEstatisticas() {
        statsCestasATextView.text = "Cestas: $cestasTimeA"
        statsFaltasATextView.text = "Faltas: $faltasTimeA"
        statsCestasBTextView.text = "Cestas: $cestasTimeB"
        statsFaltasBTextView.text = "Faltas: $faltasTimeB"
        // VERBOSE: A estatística muda a cada ponto ou falta, use o V para não poluir em debug normal.
        LogHelper.v("atualizarEstatisticas: Stats atualizadas - Time A: $cestasTimeA cestas, $faltasTimeA faltas.")
    }

    private fun destacarLider() {
        // ... (Lógica de UI) ...

        val lider = when {
            pontuacaoTimeA > pontuacaoTimeB -> "Time A"
            pontuacaoTimeB > pontuacaoTimeA -> "Time B"
            else -> "Empate"
        }
        // DEBUG: Rastreia a lógica de UI de destaque.
        LogHelper.d("destacarLider: Placar ${pontuacaoTimeA}x${pontuacaoTimeB}. Líder atual: $lider.")
    }

    fun reiniciarPartida() {
        // INFO: Ação importante de reset.
        LogHelper.i("reiniciarPartida: Resetando todos os contadores para 0.")

        pontuacaoTimeA = 0
        pTimeA.setText(pontuacaoTimeA.toString())
        pontuacaoTimeB = 0
        pTimeB.setText(pontuacaoTimeB.toString())

        cestasTimeA = 0
        cestasTimeB = 0
        faltasTimeA = 0
        faltasTimeB = 0

        Toast.makeText(this, "Placar reiniciado", Toast.LENGTH_SHORT).show()

        // WARNING: Apenas um aviso para a UI. Não é um problema do App.
        LogHelper.w("reiniciarPartida: Log de eventos reiniciado.")
        logTextView.text = "Partida:"

        atualizarEstatisticas()
        destacarLider()
    }

    private fun mostrarResultadoFinal() {
        LogHelper.d("mostrarResultadoFinal: Calculando o vencedor.")
        var vencedor = "Empate"
        // ... (Lógica de cálculo) ...

        // INFO: Resumo final da partida
        LogHelper.i("mostrarResultadoFinal: Jogo encerrado! Vencedor: $vencedor, Placar Final: A $pontuacaoTimeA x B $pontuacaoTimeB.")

        // ... (Geração da mensagem e exibição do AlertDialog) ...
    }
}