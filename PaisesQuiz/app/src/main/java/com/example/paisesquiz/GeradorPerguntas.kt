package com.example.paisesquiz

object GeradorPerguntas {

    private val paises = listOf(
        Pais("Brasil", "br"),
        Pais("Guatemala", "gt"),
        Pais("Honduras", "hn"),
        Pais("México", "mx"),
        Pais("Nova Zelândia", "nz"),
        Pais("Catar", "qa"),
        Pais("Romênia", "ro"),
        Pais("Reino Unido", "gb"),
        Pais("Estados Unidos", "us"),
        Pais("Suíça", "ch"),
        Pais("Suécia", "se"),
        Pais("Espanha", "es"),
        Pais("Paquistão", "pk"),
        Pais("Japão", "jp"),
        Pais("Líbano", "lb")
    )

    fun gerarPerguntas(): List<Pais> {
        return paises.shuffled().take(5)
    }
}