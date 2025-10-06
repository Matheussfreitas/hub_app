package com.gabrielmatheus.apphub // Pacote baseado no seu print

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.gabrielmatheus.apphub.todo.TodoActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Carrega o layout visual que criamos em activity_main.xml
        setContentView(R.layout.activity_main)

        // 1. Encontrar cada botão no layout pelo seu ID
        val btnPlacar: Button = findViewById(R.id.btnPlacarBasquete)
        val btnCalculadora: Button = findViewById(R.id.btnCalculadora)
        val btnTodoApp: Button = findViewById(R.id.btnTodoApp) // Assumindo que o ID do terceiro botão é este

        // 2. Configurar o que acontece quando o botão do Placar é clicado
        btnPlacar.setOnClickListener {
            // Cria uma "intenção" de abrir a PlacarActivity
            val intent = Intent(this, PlacarActivity::class.java)
            // Executa a intenção, abrindo a nova tela
            startActivity(intent)
        }

        // 3. Configurar o que acontece quando o botão da Calculadora é clicado
        btnCalculadora.setOnClickListener {
            // Cria uma "intenção" de abrir a CalcActivity
            val intent = Intent(this, CalcActivity::class.java)
            startActivity(intent)
        }

        // 4. Configurar o que acontece quando o botão do Novo App é clicado
        btnTodoApp.setOnClickListener {
            // Cria uma "intenção" de abrir a TodoActivity (ou o nome que você escolheu)
            val intent = Intent(this, TodoActivity::class.java)
            startActivity(intent)
        }
    }
}