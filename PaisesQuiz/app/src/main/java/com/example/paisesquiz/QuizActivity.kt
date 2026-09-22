package com.example.paisesquiz

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import java.text.Normalizer

class QuizActivity : AppCompatActivity() {

    // 1. Variáveis de estado do Quiz
    private var indiceAtual = 0
    private var pontuacao = 0
    private lateinit var perguntas: List<Pais>
    private lateinit var nomeJogador: String

    // 2. Referências dos componentes visuais
    private lateinit var tvPergunta: TextView
    private lateinit var ivBandeira: ImageView
    private lateinit var etResposta: TextInputEditText
    private lateinit var btnResponder: Button
    private lateinit var tvResultado: TextView
    private lateinit var btnProxima: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Recupera o nome do jogador enviado da tela anterior
        nomeJogador = intent.getStringExtra("NOME") ?: "Jogador"

        // Inicializa as views
        tvPergunta = findViewById(R.id.tvPergunta)
        ivBandeira = findViewById(R.id.ivBandeira)
        etResposta = findViewById(R.id.etResposta)
        btnResponder = findViewById(R.id.btnResponder)
        tvResultado = findViewById(R.id.tvResultado)
        btnProxima = findViewById(R.id.btnProxima)

        // Configura os ouvintes de clique dos botões (Item 3.2 e Item 3.3)
        btnResponder.setOnClickListener {
            responder()
        }

        btnProxima.setOnClickListener {
            avancarProximaOuFinalizar()
        }

        // Gera as 5 perguntas aleatórias
        perguntas = GeradorPerguntas.gerarPerguntas()

        // Carrega a primeira pergunta (Pergunta 1 de 5)
        carregarPergunta()
    }

    /**
     * Atualiza a interface gráfica para exibir a pergunta atual (índiceAtual)
     */
    private fun carregarPergunta() {
        val paisAtual = perguntas[indiceAtual]

        // Atualiza o contador (ex: Pergunta 1 de 5)
        tvPergunta.text = "Pergunta ${indiceAtual + 1} de ${perguntas.size}"

        // Atualiza a imagem da bandeira
        ivBandeira.setImageResource(obterIdBandeira(paisAtual.codigoBandeira))

        // Reseta os campos de entrada e feedback
        etResposta.text?.clear()
        etResposta.isEnabled = true
        tvResultado.text = ""

        // Ajusta o texto do botão na última pergunta
        if (indiceAtual == perguntas.size - 1) {
            btnProxima.text = "Ver Resultado"
        } else {
            btnProxima.text = "Próxima"
        }

        // Ajusta a visibilidade dos botões
        btnResponder.visibility = View.VISIBLE
        btnResponder.isEnabled = true
        btnProxima.visibility = View.GONE
    }

    /**
     * Item 3.2: Validação da resposta digitada e feedback visual para o jogador
     */
    private fun responder() {
        val respostaDigitada = etResposta.text?.toString()?.trim() ?: ""

        // Se o usuário não digitou nada
        if (respostaDigitada.isEmpty()) {
            etResposta.error = "Digite o nome do país!"
            return
        }

        val paisAtual = perguntas[indiceAtual]
        val respostaNormalizada = respostaDigitada.normalizar()
        val nomeCorretoNormalizado = paisAtual.nome.normalizar()

        // Oculta o teclado virtual
        ocultarTeclado()

        // Comparação da resposta
        if (respostaNormalizada == nomeCorretoNormalizado) {
            pontuacao++
            tvResultado.text = "Resposta Correta! 🎉"
            tvResultado.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
        } else {
            tvResultado.text = "Incorreto! O país correto é ${paisAtual.nome}."
            tvResultado.setTextColor(ContextCompat.getColor(this, R.color.primary_red))
        }

        // Travar campo de resposta e botão de envio
        etResposta.isEnabled = false
        btnResponder.isEnabled = false

        // Exibir o botão para ir para a próxima pergunta
        btnProxima.visibility = View.VISIBLE
    }

    /**
     * Item 3.3: Avança para a próxima pergunta ou redireciona para a Tela Final (Passo 4)
     */
    private fun avancarProximaOuFinalizar() {
        if (indiceAtual < perguntas.size - 1) {
            indiceAtual++
            carregarPergunta()
        } else {
            // Transição para a tela final de resultado (Passo 4)
            val intent = Intent(this, ResultadoActivity::class.java)
            intent.putExtra("NOME", nomeJogador)
            intent.putExtra("PONTUACAO", pontuacao)
            intent.putExtra("TOTAL", perguntas.size)
            startActivity(intent)
            finish()
        }
    }

    /**
     * Função auxiliar para remover acentos, converter para minúsculas e ignorar diferenças simples
     */
    private fun String.normalizar(): String {
        val unaccented = Normalizer.normalize(this, Normalizer.Form.NFD)
        return Regex("\\p{InCombiningDiacriticalMarks}+").replace(unaccented, "").lowercase()
    }

    /**
     * Fecha o teclado virtual
     */
    private fun ocultarTeclado() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        currentFocus?.let { view ->
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    /**
     * Mapeia o código do país para o recurso drawable correspondente
     */
    private fun obterIdBandeira(codigo: String): Int {
        return when (codigo) {
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
    }
}