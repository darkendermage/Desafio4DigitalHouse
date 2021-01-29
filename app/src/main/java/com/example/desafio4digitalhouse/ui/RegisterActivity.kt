package com.example.desafio4digitalhouse.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.desafio4digitalhouse.databinding.ActivityRegisterBinding
import com.example.desafio4digitalhouse.models.User
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        binding.btnCreate.setOnClickListener {
            cadastrarUsuario()
            callMain()
        }

        binding.registerBack.setOnClickListener {
            finish()
        }

        setContentView(binding.root)
    }

    private fun callMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun cadastrarUsuario() {
        val usuario = getUsuario()
        if (usuario != null) {
            sendFirebaseCad(usuario)
        }
        else {
            sendMsg("Preencha o campo email e senha corretamente!")
        }

    }

    private fun sendFirebaseCad(usuario: User) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(usuario.email, usuario.senha)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    val firebaseUser = it.result?.user!!
                    User(firebaseUser.email.toString(),"",firebaseUser.uid)
                    sendMsg("Usuario cadastrado com sucesso!")
                }

            }.addOnFailureListener {
                sendMsg("Erro ao cadastrar. ")
            }
    }

    private fun sendMsg(msg : String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun getUsuario(): User? {
        var nome = binding.etNameCadastro.text.toString()
        var email = binding.etEmailCadastro.text.toString()
        var senha = binding.etSenhaCadastro.text.toString()

        return if (!email.isNullOrEmpty() and !senha.isNullOrEmpty()) {
            User(nome, email, senha)
        } else {
            null
        }
    }
}