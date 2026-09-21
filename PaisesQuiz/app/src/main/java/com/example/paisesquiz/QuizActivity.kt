package com.example.paisesquiz
import android.widget.ImageView
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class QuizActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        val nomeJogador = intent.getSerializableExtra("NOME") as String

        val perguntas = GeradorPerguntas.gerarPerguntas()
        val imagemBandeira = findViewById<ImageView>(R.id.ivBandeira)

        val primeiraPergunta = perguntas[0]

        val idBandeira = when (primeiraPergunta.codigoBandeira) {
            "br" -> R.drawable.brasil
            "gt" -> R.drawable.guatemala
            "hn" -> R.drawable.honduras
            "mx" -> R.drawable.mexico
            "nz" -> R.drawable.novazelandia
            "qa" -> R.drawable.catar
            "ro" -> R.drawable.romenia
            "gb" -> R.drawable.reinounido
            "us" -> R.drawable.estadosunidos
            "ch" -> R.drawable.suica
            "se" -> R.drawable.suecia
            "es" -> R.drawable.espanha
            "pk" -> R.drawable.paquistao
            "jp" -> R.drawable.japao
            "lb" -> R.drawable.libano
            else -> 0
        }

        imagemBandeira.setImageResource(idBandeira)
    }
}
