package com.example.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.app3.App3Activity
import com.example.basquete_app.BasqueteActivity
import com.example.calc_app.CalcActivity
import com.example.calc_app.R
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCalc = findViewById<Button>(R.id.btnCalculadora)
        val btnBasq = findViewById<Button>(R.id.btnBasquete)
        val btnQuiz = findViewById<Button>(R.id.btnQuiz)

        btnCalc.setOnClickListener {
            startActivity(Intent(this, CalcActivity::class.java))
        }

        btnBasq.setOnClickListener {
            startActivity(Intent(this, BasqueteActivity::class.java))
        }

        btnQuiz.setOnClickListener {
            startActivity(Intent(this, App3Activity::class.java))
        }
    }
}