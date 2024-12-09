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
        const val PREFS_NAME = "app_preferences"
        const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Comprobar si el usuario ya está logueado
        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        if (sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)) {
            goToMainActivity()
            return
        }

        // Validar usuario y contraseña
        binding.buttonValidate.setOnClickListener {
            val user = binding.editTextUser.text.toString()
            val pass = binding.editTextPass.text.toString()

            if (user == MYUSER && pass == MYPASS) {
                // Guardar estado de sesión en SharedPreferences
                sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, true).apply()
                goToMainActivity()
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Evitar volver a la pantalla de login al presionar "Atrás"
    }
}
