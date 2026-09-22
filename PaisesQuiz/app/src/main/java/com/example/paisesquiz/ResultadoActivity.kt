package com.example.paisesquiz

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ResultadoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_resultado)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nomeJogador = intent.getSerializableExtra("NOME") as String
        val pontuacao = intent.getIntExtra("PONTUACAO", 0)

        findViewById<TextView>(R.id.tvNomeResultado).text = nomeJogador
        findViewById<TextView>(R.id.tvPontuacaoResultado).text =
            getString(R.string.formato_pontuacao_final, pontuacao)
    }

    fun jogarNovamente(view: View) {
        val intent = Intent(this, PaginaInicialActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
