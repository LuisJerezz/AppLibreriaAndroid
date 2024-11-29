package com.example.proyectoandroid


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectoandroid.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    companion object {
        const val MYUSER = "admin"
        const val MYPASS = "1234"
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonValidate.setOnClickListener {
            val user = binding.editTextUser.text.toString()
            val pass = binding.editTextPass.text.toString()

            if (user == MYUSER && pass == MYPASS) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("USERNAME", user)
                intent.putExtra("PASSWORD", pass)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
