package com.example.proyectoandroid


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectoandroid.adapters.UsuarioAdapter
import com.example.proyectoandroid.databinding.ActivityMainBinding
import com.example.proyectoandroid.models.Usuario

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var usuarioAdapter: UsuarioAdapter
    private val usuarios = mutableListOf<Usuario>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar lista de usuarios
        initUsuarios()

        // Configurar RecyclerView con el adaptador
        usuarioAdapter = UsuarioAdapter(usuarios)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = usuarioAdapter
        }
    }

    private fun initUsuarios() {
        usuarios.add(Usuario(1, "Juan Pérez", "juan@example.com"))
        usuarios.add(Usuario(2, "María Gómez", "maria@example.com"))
        usuarios.add(Usuario(3, "Carlos López", "carlos@example.com"))
        usuarios.add(Usuario(4, "Ana Fernández", "ana@example.com"))
        usuarios.add(Usuario(5, "Pedro Sánchez", "pedro@example.com"))
        usuarios.add(Usuario(6, "Lucía Martínez", "lucia@example.com"))
        usuarios.add(Usuario(7, "Miguel Rodríguez", "miguel@example.com"))
        usuarios.add(Usuario(8, "Sofía Ramírez", "sofia@example.com"))
        usuarios.add(Usuario(9, "Raúl Torres", "raul@example.com"))
        usuarios.add(Usuario(10, "Isabel Herrera", "isabel@example.com"))
        usuarios.add(Usuario(11, "Javier Morales", "javier@example.com"))
        usuarios.add(Usuario(12, "Elena Navarro", "elena@example.com"))
    }

}
