package com.example.desafio4digitalhouse.models

import java.io.Serializable

data class User(val nome : String, val email: String, val senha : String, val id: String = "") : Serializable