package com.example.proyectoandroid

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectoandroid.data.AuthResponse
import com.example.proyectoandroid.data.service.LoginRequest
import com.example.proyectoandroid.data.service.RetrofitConnection
import com.example.proyectoandroid.databinding.ActivityLoginBinding
import com.example.proyectoandroid.ui.views.activity.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("login-info", MODE_PRIVATE)

        // Auto-login si hay credenciales
        val savedEmail = sharedPreferences.getString("email", null)
        val savedPassword = sharedPreferences.getString("password", null)
        if (savedEmail != null && savedPassword != null) {
            binding.editTextUser.setText(savedEmail)
            binding.editTextPass.setText(savedPassword)
            performLogin(savedEmail, savedPassword, isAutoLogin = true)
        }

        binding.buttonRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.buttonValidate.setOnClickListener {
            val email = binding.editTextUser.text.toString().trim()
            val password = binding.editTextPass.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            performLogin(email, password)
        }
    }

    private fun performLogin(email: String, password: String, isAutoLogin: Boolean = false) {
        val loginRequest = LoginRequest(email, password)

        RetrofitConnection.provideApiService(this).login(loginRequest).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    response.body()?.token?.let { token ->
                        saveCredentials(email, password, token)
                        Toast.makeText(this@LoginActivity, "Inicio de sesión correcto", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Credenciales incorrectas", Toast.LENGTH_LONG).show()
                    if (isAutoLogin) clearCredentials()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error de conexión: ${t.message}", Toast.LENGTH_LONG).show()
                if (isAutoLogin) clearCredentials()
            }
        })
    }

    private fun saveCredentials(email: String, password: String, token: String) {
        sharedPreferences.edit().apply {
            putString("email", email)
            putString("password", password)
            putString("jwt_token", token)
            apply()
        }
        Log.d("LoginActivity", "Credenciales guardadas")
    }

    private fun clearCredentials() {
        sharedPreferences.edit().clear().apply()
    }
}