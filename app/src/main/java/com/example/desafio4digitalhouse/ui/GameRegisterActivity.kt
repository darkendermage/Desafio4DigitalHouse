package com.example.desafio4digitalhouse.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.desafio4digitalhouse.databinding.ActivityGameRegisterBinding
import com.example.desafio4digitalhouse.models.Game
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso


class GameRegisterActivity : AppCompatActivity() {
    private val CODE_IMG: Int = 100
    private lateinit var storageReference: StorageReference
    private var game: Game = Game()
    lateinit var binding: ActivityGameRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameRegisterBinding.inflate(layoutInflater)

        binding.registerGameBack.setOnClickListener {
            finish()
        }

        binding.photo.setOnClickListener {
            carregarImagem()
        }

        binding.ivBtnCadastroGame.setOnClickListener {
            game.nome = binding.gameName.text.toString()
            game.dataCriacao = binding.date.text.toString()
            game.descricao = binding.description.text.toString()

            if (game.urlImg == "") {
                sendMsg("Adicione uma imagem antes de salvar!")
            } else {
                salvarDados()
                finish()
            }
        }

        setContentView(binding.root)
    }

    private fun sendMsg(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CODE_IMG) {

            val uploadFile = storageReference.putFile(data!!.data!!)
            uploadFile.continueWithTask { task ->
                if (task.isSuccessful) {
                    sendMsg("Imagem Carrregada com sucesso!")
                }
                storageReference!!.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val url = downloadUri!!.toString()
                        .substring(0, downloadUri.toString().indexOf("&token"))

                    Log.i("URL da Imagem", url)
                    game.urlImg = url

                    Picasso.get().load(url).into(binding.cam)
                }
            }
        }
    }

    fun carregarImagem() {
        storageReference = FirebaseStorage.getInstance().getReference(getUniqueKey())
        val intent = Intent()
        intent.type = "image/"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Get Image"), CODE_IMG)
    }

    private fun getUniqueKey() = FirebaseFirestore.getInstance().collection("pegando chave").document().id

    fun salvarDados() {
        val bancoDados = FirebaseFirestore.getInstance().collection("InfoGame")
        val id = getUniqueKey()
        game.id = id
        bancoDados.document(id).set(game)
    }
}