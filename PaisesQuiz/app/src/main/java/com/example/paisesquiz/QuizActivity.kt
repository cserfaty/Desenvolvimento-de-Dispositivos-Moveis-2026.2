package com.example.paisesquiz

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.text.Normalizer

class QuizActivity : AppCompatActivity() {

    private lateinit var nomeJogador: String
    private lateinit var perguntas: List<Pais>
    private var indiceAtual = 0
    private var pontuacao = 0
    private var respostaValidada = false

    private lateinit var tvPergunta: TextView
    private lateinit var ivBandeira: ImageView
    private lateinit var etResposta: TextInputEditText
    private lateinit var btnResponder: MaterialButton
    private lateinit var tvResultado: TextView
    private lateinit var btnProxima: MaterialButton

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

        nomeJogador = intent.getSerializableExtra("NOME") as String
        perguntas = GeradorPerguntas.gerarPerguntas()

        tvPergunta = findViewById(R.id.tvPergunta)
        ivBandeira = findViewById(R.id.ivBandeira)
        etResposta = findViewById(R.id.etResposta)
        btnResponder = findViewById(R.id.btnResponder)
        tvResultado = findViewById(R.id.tvResultado)
        btnProxima = findViewById(R.id.btnProxima)

        btnResponder.setOnClickListener { verificarResposta() }
        btnProxima.setOnClickListener { proximaPergunta() }

        exibirPergunta()
    }

    private fun exibirPergunta() {
        val pergunta = perguntas[indiceAtual]

        tvPergunta.text = getString(R.string.formato_contador_pergunta, indiceAtual + 1, perguntas.size)

        val idBandeira = when (pergunta.codigoBandeira) {
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
        ivBandeira.setImageResource(idBandeira)

        etResposta.text = null
        etResposta.isEnabled = true
        tvResultado.text = ""
        btnResponder.isEnabled = true
        btnResponder.visibility = View.VISIBLE
        btnProxima.visibility = View.GONE
        respostaValidada = false
    }

    private fun normalizar(texto: String): String {
        val semAcentos = Normalizer.normalize(texto, Normalizer.Form.NFD)
            .replace(Regex("\\p{Mn}+"), "")
        return semAcentos.trim().lowercase()
    }

    private fun verificarResposta() {
        if (respostaValidada) return

        val pergunta = perguntas[indiceAtual]
        val resposta = etResposta.text?.toString()?.trim().orEmpty()
        val correta = normalizar(resposta) == normalizar(pergunta.nome)

        if (correta) {
            pontuacao += PONTOS_POR_ACERTO
            tvResultado.setTextColor(getColor(R.color.correct_green))
            tvResultado.text = getString(R.string.resultado_correto)
        } else {
            tvResultado.setTextColor(getColor(R.color.incorrect_red))
            tvResultado.text = getString(R.string.resultado_incorreto, pergunta.nome)
        }

        respostaValidada = true
        etResposta.isEnabled = false
        btnResponder.isEnabled = false
        btnProxima.text = if (indiceAtual == perguntas.lastIndex) {
            getString(R.string.btn_ver_resultado)
        } else {
            getString(R.string.btn_proxima)
        }
        btnProxima.visibility = View.VISIBLE
    }

    private fun proximaPergunta() {
        indiceAtual++

        if (indiceAtual < perguntas.size) {
            exibirPergunta()
        } else {
            val intent = Intent(this, ResultadoActivity::class.java)
            intent.putExtra("NOME", nomeJogador)
            intent.putExtra("PONTUACAO", pontuacao)
            startActivity(intent)
            finish()
        }
    }

    companion object {
        private const val PONTOS_POR_ACERTO = 20
    }
}
