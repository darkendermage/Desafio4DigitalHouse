package com.example.desafio4digitalhouse.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.desafio4digitalhouse.adapters.GameAdapter
import com.example.desafio4digitalhouse.databinding.ActivityMainBinding
import com.example.desafio4digitalhouse.models.Game
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : AppCompatActivity(), GameAdapter.onGameClickListener {
    lateinit var binding: ActivityMainBinding

    private var listaGames = MutableLiveData<ArrayList<Game>>()
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: GameAdapter
    val scope = CoroutineScope(Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        layoutManager = GridLayoutManager(this, 2)
        binding.rvGames.layoutManager = layoutManager
        binding.rvGames.setHasFixedSize(true)
        listaGames.observe(this, {
            adapter = GameAdapter(it, this)
            binding.rvGames.adapter = adapter
        })

        binding.addNewGame.setOnClickListener {
            callGameRegister()
        }

        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        getGames()
    }

    fun callGameRegister() {
        val intent = Intent(this, GameRegisterActivity::class.java)
        startActivity(intent)
    }

    fun getGames() {
        val bancoDados = Firebase.firestore.collection("InfoGame")
        val listaGamesLocal = ArrayList<Game>()
        scope.launch {
            val listaGamesRemoto = bancoDados.get().await()
            listaGamesRemoto.forEach { doc->
                listaGamesLocal.add(doc.toObject())
            }
            listaGames.postValue(listaGamesLocal)
        }
    }

    override fun GameClick(position: Int) {
        val intent = Intent(this, GameDetailsActivity::class.java)
        val game = listaGames.value?.get(position)

        intent.putExtra("game", game)

        startActivity(intent)
    }
}