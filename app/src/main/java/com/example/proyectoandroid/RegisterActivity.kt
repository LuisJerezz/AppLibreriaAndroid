package com.example.proyectoandroid

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectoandroid.data.User
import com.example.proyectoandroid.data.service.LoginRequest
import com.example.proyectoandroid.data.service.RetrofitConnection
import com.example.proyectoandroid.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonRegisterUser.setOnClickListener {
            val dni = binding.editDni.text.toString().trim()
            val name = binding.editName.text.toString().trim()
            val email = binding.editEmail.text.toString().trim()
            val password = binding.editPassword.text.toString().trim()
            val confirmPassword = binding.editConfirmPassword.text.toString().trim()
            val phone = binding.editPhone.text.toString().trim()

            if (!validarCampos(dni, name, email, password, confirmPassword, phone)) return@setOnClickListener

            val user = User(
                dni = dni,
                name = name,
                email = email,
                password = password,
                phone = phone,
                image = "https://picsum.photos/200/300", // Imagen por defecto
                disponible = true // Disponibilidad por defecto
            )

            registrarUsuario(user)
        }

        binding.buttonGoToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun validarCampos(
        dni: String,
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        phone: String
    ): Boolean {
        return when {
            dni.isEmpty() || name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() -> {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                false
            }
            !dni.matches(Regex("^[0-9]{8}[A-Za-z]\$")) -> {
                Toast.makeText(this, "DNI inválido (8 números + letra)", Toast.LENGTH_SHORT).show()
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show()
                false
            }
            password != confirmPassword -> {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                false
            }
            password.length < 6 -> {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                false
            }
            !phone.matches(Regex("^[6-9][0-9]{8}\$")) -> {
                Toast.makeText(this, "Teléfono inválido (9 números)", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun registrarUsuario(user: User) {
        RetrofitConnection.provideApiService(this).register(user).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "Registro exitoso", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@RegisterActivity, "Error en el registro: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Error de conexión: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}