package com.example.paisesquiz

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import kotlin.jvm.java

class PaginaInicialActivity : AppCompatActivity() {

    private lateinit var etNome: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etNome = findViewById(R.id.etNome)
    }

    fun iniciar(view: View) {
        val nomeJogador = etNome.text?.toString()?.trim()

        if (nomeJogador.isNullOrEmpty()) {
            etNome.error = getString(R.string.error_empty_name)
            Toast.makeText(this, R.string.error_empty_name, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Bem-vindo(a), $nomeJogador!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("NOME", nomeJogador)
            startActivity(intent)
        }
    }
}