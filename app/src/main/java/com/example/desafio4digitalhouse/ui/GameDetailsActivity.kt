package com.example.desafio4digitalhouse.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.desafio4digitalhouse.databinding.ActivityGameDetailsBinding
import com.example.desafio4digitalhouse.models.Game
import com.squareup.picasso.Picasso

class GameDetailsActivity : AppCompatActivity() {
    private var game: Game = Game()
    lateinit var binding: ActivityGameDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameDetailsBinding.inflate(layoutInflater)

        var game = intent.getSerializableExtra("game") as? Game

        if (game != null) {
            game = game
            updateInfoGame(game)
        } else {
            Toast.makeText(this, "Não foi possível carregar suas as informações!", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.ivBackDetail.setOnClickListener {
            finish()
        }

        //botão para editar as informações do game
        binding.ivEditGame.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra("game", game)

            startActivity(intent)
            finish()
        }

        setContentView(binding.root)
    }

    //colocando as informações do game no layout
    fun updateInfoGame(game: Game) {
        binding.tvGameName.text = game.nome
        binding.tvDataGame.text = game.dataCriacao
        binding.tvDescricaoGame.text = game.descricao

        Picasso.get().load(game.urlImg).into(binding.ivBgImg)
    }
}