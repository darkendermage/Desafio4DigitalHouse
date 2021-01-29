package com.example.desafio4digitalhouse.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.desafio4digitalhouse.databinding.ActivityLoginBinding
import com.example.desafio4digitalhouse.models.User
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        binding.ivBtnLogin.setOnClickListener {
            realizarLogin()
            callMain()
        }

        binding.tvCreate.setOnClickListener {
            callRegister()
        }

        setContentView(binding.root)
    }

    private fun callMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun callRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun realizarLogin() {
        val usuario = getUsuario()
        if (usuario != null) {
            sendFirebaseLogin(usuario)
        } else {
            sendMsg("Favor preencher os campos acima.")
        }
    }

    private fun sendMsg(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun sendFirebaseLogin(usuario: User) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(usuario.email, usuario.senha)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val firebaseUser = it.result?.user!!
                    User(firebaseUser.email.toString(), "", firebaseUser.uid)
                    sendMsg("Login realizado com sucesso")
                }
                if (it.isCanceled) {
                    sendMsg("Não foi possível fazer o login, tente novamente")
                }
            }
    }

    private fun getUsuario(): User? {
        var email = binding.etEmailLogin.text.toString()
        var senha = binding.etPasswordLogin.text.toString()

        return if (!email.isNullOrBlank() and !senha.isNullOrBlank()) {
            User("", email, senha)
        } else {
            null
        }
    }
}