package com.gabrielmatheus.apphub.todo

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gabrielmatheus.apphub.LogHelper
import com.gabrielmatheus.apphub.R
import java.util.Calendar

class TodoActivity : AppCompatActivity() {

    private val tasks = mutableListOf<Task>() // Nota: Este objeto não está sendo atualizado no adapter.addTask,
    // o que pode causar um bug na persistência. (Ver Análise de Bug no Roteiro 2).
    private val adapter = TodoAdapter(mutableListOf()) // Adapter está inicializado com lista vazia
    private var selectedDate = ""

    private lateinit var recyclerViewTasks: RecyclerView
    private lateinit var buttonPickDate: Button
    private lateinit var buttonAddTask: Button
    private lateinit var editTextTask: EditText
    private lateinit var spinnerPriority: Spinner
    private lateinit var textViewDate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_todo)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // INFO: Registro de ciclo de vida
        LogHelper.i("TodoActivity: onCreate chamado. savedInstanceState é ${if (savedInstanceState == null) "NULO" else "EXISTENTE"}")

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // DEBUG: Rastreia a ligação dos componentes
        LogHelper.d("TodoActivity: Ligando views do layout.")
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks)
        buttonPickDate = findViewById(R.id.buttonPickDate)
        buttonAddTask = findViewById(R.id.buttonAddTask)
        editTextTask = findViewById(R.id.editTextTask)
        spinnerPriority = findViewById(R.id.spinnerPriority)
        textViewDate = findViewById(R.id.textViewDate)

        recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        recyclerViewTasks.adapter = adapter

        buttonPickDate.setOnClickListener {
            LogHelper.i("buttonPickDate: Iniciando seleção de data.") // INFO: Ação importante
            pickDate()
        }

        buttonAddTask.setOnClickListener {
            val title = editTextTask.text.toString()
            if (title.isEmpty()) {
                LogHelper.w("buttonAddTask: Tentativa de adicionar tarefa sem título. Ação bloqueada.") // WARNING: Ação do usuário inesperada.
                return@setOnClickListener
            }

            val priority = spinnerPriority.selectedItem.toString()
            val task = Task(title, if (selectedDate.isEmpty()) "Sem data" else selectedDate, priority)

            // O adapter deve ter um método que atualiza ambas as listas (interna do adapter E a lista 'tasks' da Activity)
            // Assumindo que o método addTask do adapter também adiciona à lista 'tasks' da Activity se for necessário para a persistência.
            adapter.addTask(task)

            // DEBUG: Detalhes da tarefa adicionada para fins de rastreio
            LogHelper.d("buttonAddTask: Tarefa criada. Título: '$title', Prioridade: $priority, Vencimento: ${task.dueDate}")
            LogHelper.i("buttonAddTask: Tarefa adicionada com sucesso.") // INFO: Evento importante.

            editTextTask.text.clear()
            textViewDate.text = "Nenhuma data selecionada"
            selectedDate = ""
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Usamos a lista 'tasks' da Activity para persistência
        outState.putSerializable("tasks_list", ArrayList(tasks))
        LogHelper.d("onSaveInstanceState: Salvando ${tasks.size} tarefas. Lista do adapter pode ser diferente.") // DEBUG: Confirma que estamos salvando.
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        LogHelper.d("onRestoreInstanceState: Tentando restaurar tarefas.") // DEBUG: Confirma que estamos restaurando.

        val savedTasks = savedInstanceState.getSerializable("tasks_list") as? ArrayList<Task>
        if (savedTasks != null) {
            tasks.clear()
            tasks.addAll(savedTasks)
            // É essencial que o adapter seja atualizado com os dados restaurados (ex: adapter.setTasks(tasks))
            // Assumindo que notifyDataSetChanged() no final está sendo usado para atualizar o RecyclerView.
            // Para garantir que o adapter tenha os dados corretos, seria melhor ter um método tipo: adapter.replaceTasks(tasks)
            adapter.notifyDataSetChanged()
            LogHelper.i("onRestoreInstanceState: ${savedTasks.size} tarefas restauradas com sucesso.")
        } else {
            // ERROR: Falha crítica na restauração que pode levar à perda de dados.
            LogHelper.e("onRestoreInstanceState: Falha crítica! A lista salva era nula ou o cast falhou.")
        }
    }

    private fun pickDate() {
        LogHelper.d("pickDate: Abrindo seletor de data.") // DEBUG: Rastreia a ação do usuário.
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, { _, y, m, d ->
            selectedDate = "$d/${m + 1}/$y"
            textViewDate.text = selectedDate
            LogHelper.v("pickDate: Callback do seletor. Data selecionada: $selectedDate") // VERBOSE: Detalhe da data exata.
        }, year, month, day)

        dpd.show()
    }
}
