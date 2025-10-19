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

    private val tasks = mutableListOf<Task>()
    private val adapter = TodoAdapter(mutableListOf())
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
        LogHelper.i("TodoActivity: onCreate chamado. savedInstanceState é ${if (savedInstanceState == null) "NULO" else "EXISTENTE"}")

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerViewTasks = findViewById(R.id.recyclerViewTasks)
        buttonPickDate = findViewById(R.id.buttonPickDate)
        buttonAddTask = findViewById(R.id.buttonAddTask)
        editTextTask = findViewById(R.id.editTextTask)
        spinnerPriority = findViewById(R.id.spinnerPriority)
        textViewDate = findViewById(R.id.textViewDate)

        recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        recyclerViewTasks.adapter = adapter

        buttonPickDate.setOnClickListener {
            pickDate()
        }

        buttonAddTask.setOnClickListener {
            val title = editTextTask.text.toString()
            if (title.isEmpty()) {
                LogHelper.w("buttonAddTask: Tentativa de adicionar tarefa sem título.") // WARNING: Ação do usuário inesperada.
                return@setOnClickListener
            }

            val priority = spinnerPriority.selectedItem.toString()
            val task = Task(title, if (selectedDate.isEmpty()) "Sem data" else selectedDate, priority)
            adapter.addTask(task)
            LogHelper.i("buttonAddTask: Tarefa adicionada -> '$title'") // INFO: Evento importante.

            editTextTask.text.clear()
            textViewDate.text = "Nenhuma data selecionada"
            selectedDate = ""
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // A chave "tasks_list" é usada para salvar e recuperar a lista.
        outState.putSerializable("tasks_list", ArrayList(tasks))
        LogHelper.d("onSaveInstanceState: Salvando ${tasks.size} tarefas.") // DEBUG: Confirma que estamos salvando.
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        LogHelper.d("onRestoreInstanceState: Tentando restaurar tarefas.") // DEBUG: Confirma que estamos restaurando.
        // Recupera a lista salva. O 'as?' faz um cast seguro.
        val savedTasks = savedInstanceState.getSerializable("tasks_list") as? ArrayList<Task>
        if (savedTasks != null) {
            tasks.clear()
            tasks.addAll(savedTasks)
            adapter.notifyDataSetChanged() // Notifica o adapter que os dados mudaram completamente.
            LogHelper.i("onRestoreInstanceState: ${savedTasks.size} tarefas restauradas com sucesso.")
        } else {
            LogHelper.e("onRestoreInstanceState: Falha ao restaurar tarefas. A lista salva era nula.")
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
            LogHelper.v("pickDate: Data selecionada: $selectedDate") // VERBOSE: Detalhe da data exata.
        }, year, month, day)

        dpd.show()
    }
}
