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
import com.gabrielmatheus.apphub.R
import java.util.Calendar

class TodoActivity : AppCompatActivity() {

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
            val priority = spinnerPriority.selectedItem.toString()

            if (title.isNotEmpty()) {
                val task = Task(title, if (selectedDate.isEmpty()) "Sem data" else selectedDate, priority)
                adapter.addTask(task)
                editTextTask.text.clear()
                textViewDate.text = "Nenhuma data selecionada"
                selectedDate = ""
            }
        }
    }

    private fun pickDate() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, { _, y, m, d ->
            selectedDate = "$d/${m + 1}/$y"
            textViewDate.text = selectedDate
        }, year, month, day)

        dpd.show()
    }
}
