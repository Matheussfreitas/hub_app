package com.gabrielmatheus.apphub // Pacote baseado no seu print

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.gabrielmatheus.apphub.todo.TodoActivity

class MainActivity : AppCompatActivity() {

    // Nível INFO: Usado para registrar eventos importantes como a criação da Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogHelper.i("MainActivityHub: onCreate iniciado. Carregando layout.")

        setContentView(R.layout.activity_main)

        // Nível DEBUG: Usado para rastrear o processo de ligação de componentes
        LogHelper.d("MainActivityHub: Tentando encontrar botões no layout.")
        val btnPlacar: Button = findViewById(R.id.btnPlacarBasquete)
        val btnCalculadora: Button = findViewById(R.id.btnCalculadora)
        val btnTodoApp: Button = findViewById(R.id.btnTodoApp)

        // 2. Configurar o que acontece quando o botão do Placar é clicado
        btnPlacar.setOnClickListener {
            // Nível INFO: O clique no botão é um evento importante de navegação
            LogHelper.i("MainActivityHub: Botão 'Placar de Basquete' clicado. Iniciando PlacarActivity.")
            val intent = Intent(this, PlacarActivity::class.java)
            startActivity(intent)
        }

        // 3. Configurar o que acontece quando o botão da Calculadora é clicado
        btnCalculadora.setOnClickListener {
            // Nível INFO: O clique no botão é um evento importante de navegação
            LogHelper.i("MainActivityHub: Botão 'Calculadora' clicado. Iniciando CalcActivity.")
            val intent = Intent(this, CalcActivity::class.java)
            startActivity(intent)
        }

        // 4. Configurar o que acontece quando o botão do Novo App é clicado
        btnTodoApp.setOnClickListener {
            // Nível INFO: O clique no botão é um evento importante de navegação
            LogHelper.i("MainActivityHub: Botão 'Todo App' clicado. Iniciando TodoActivity.")
            val intent = Intent(this, TodoActivity::class.java)
            startActivity(intent)
        }

        LogHelper.i("MainActivityHub: onCreate finalizado. Hub pronto para uso.")
    }
}