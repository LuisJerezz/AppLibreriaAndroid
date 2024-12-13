package com.example.proyectoandroid

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectoandroid.adapters.UsuarioAdapter
import com.example.proyectoandroid.databinding.ActivityMainBinding
import com.example.proyectoandroid.models.Usuario
import com.example.proyectoandroid.utils.PreferencesHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var usuarioAdapter: UsuarioAdapter
    private val usuarios = mutableListOf<Usuario>()
    private var currentUserId = 1 // Para generar IDs únicos automáticamente

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuración del Toolbar
        setSupportActionBar(binding.toolbar)  // Asegúrate de que tienes un Toolbar en el XML

        // Cargar usuarios desde SharedPreferences o inicializar con valores predeterminados
        if (PreferencesHelper.isFirstRun(this)) {
            initializeDefaultUsers()
        }
        usuarios.addAll(PreferencesHelper.getUsuarios(this))
        if (usuarios.isNotEmpty()) {
            currentUserId = usuarios.maxOf { it.id } + 1
        }

        usuarioAdapter = UsuarioAdapter(usuarios,
            onEditClick = { usuario, position -> showEditUserDialog(usuario, position) },
            onDeleteClick = { position -> deleteUser(position) }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = usuarioAdapter
        }

        binding.btnAddUser.setOnClickListener { showAddUserDialog() }
    }

    private fun initializeDefaultUsers() {
        val defaultUsers = listOf(
            Usuario(1, "Juan Pérez", "juan@example.com"),
            Usuario(2, "María Gómez", "maria@example.com"),
            Usuario(3, "Carlos López", "carlos@example.com"),
            Usuario(4, "Ana Fernández", "ana@example.com"),
            Usuario(5, "Pedro Sánchez", "pedro@example.com")
        )
        PreferencesHelper.saveUsuarios(this, defaultUsers)
    }

    private fun showAddUserDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_edit_user, null)
        val inputName = dialogView.findViewById<TextInputEditText>(R.id.etUserName)
        val inputEmail = dialogView.findViewById<TextInputEditText>(R.id.etUserEmail)

        MaterialAlertDialogBuilder(this)
            .setTitle("Añadir Usuario")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val name = inputName.text.toString()
                val email = inputEmail.text.toString()
                if (name.isNotEmpty() && email.isNotEmpty()) {
                    usuarios.add(Usuario(currentUserId++, name, email))
                    usuarioAdapter.notifyItemInserted(usuarios.size - 1)
                    saveUsuariosToPreferences()
                } else {
                    Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showEditUserDialog(usuario: Usuario, position: Int) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_edit_user, null)
        val inputName = dialogView.findViewById<TextInputEditText>(R.id.etUserName)
        val inputEmail = dialogView.findViewById<TextInputEditText>(R.id.etUserEmail)

        inputName.setText(usuario.nombre)
        inputEmail.setText(usuario.email)

        MaterialAlertDialogBuilder(this)
            .setTitle("Editar Usuario")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val name = inputName.text.toString()
                val email = inputEmail.text.toString()
                if (name.isNotEmpty() && email.isNotEmpty()) {
                    usuario.nombre = name
                    usuario.email = email
                    usuarioAdapter.notifyItemChanged(position)
                    saveUsuariosToPreferences()
                } else {
                    Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deleteUser(position: Int) {
        usuarios.removeAt(position)
        usuarioAdapter.notifyItemRemoved(position)
        usuarioAdapter.notifyItemRangeChanged(position, usuarios.size)
        saveUsuariosToPreferences()
    }

    private fun saveUsuariosToPreferences() {
        PreferencesHelper.saveUsuarios(this, usuarios)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun logout() {
        // Borrar la sesión de SharedPreferences
        PreferencesHelper.clearSession(this)

        // Cerrar sesión en Firebase
        FirebaseAuth.getInstance().signOut()

        // Mostrar un mensaje de confirmación
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()

        // Redirigir al LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()  // Finaliza MainActivity para evitar que el usuario regrese
    }

}
