package com.example.desafio4digitalhouse.models

import java.io.Serializable

data class Game (var nome: String = "", var dataCriacao: String = "", var descricao: String = "", var urlImg: String = "", var id: String = "") : Serializable