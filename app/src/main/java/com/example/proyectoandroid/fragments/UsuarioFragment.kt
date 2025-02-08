package com.example.proyectoandroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectoandroid.adapters.UsuarioAdapter
import com.example.proyectoandroid.databinding.FragmentUsuarioBinding
import com.example.proyectoandroid.models.Usuario
import com.example.proyectoandroid.ui.MainActivity
import com.example.proyectoandroid.utils.PreferencesHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class UsuarioFragment : Fragment() {

    private lateinit var binding: FragmentUsuarioBinding
    private lateinit var usuarioAdapter: UsuarioAdapter
    private val usuarios = mutableListOf<Usuario>()
    private var currentUserId = 1 // Para generar IDs únicos automáticamente
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUsuarioBinding.inflate(inflater, container, false)

        // Obtener el NavController de la actividad principal
        //navController = (activity as MainActivity).navController

        // Cargar usuarios desde SharedPreferences o inicializar con valores predeterminados
        if (PreferencesHelper.isFirstRun(requireContext())) {
            initializeDefaultUsers()
        }
        usuarios.addAll(PreferencesHelper.getUsuarios(requireContext()))
        if (usuarios.isNotEmpty()) {
            currentUserId = usuarios.maxOf { it.id } + 1
        }

        usuarioAdapter = UsuarioAdapter(usuarios,
            onEditClick = { usuario, position -> showEditUserDialog(usuario, position) },
            onDeleteClick = { position -> deleteUser(position) }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = usuarioAdapter
        }

        binding.btnAddUser.setOnClickListener { showAddUserDialog() }

        // Agregar funcionalidad para navegar a otros fragmentos desde el UsuarioFragment
        binding.recyclerView.setOnClickListener {
            navigateToFragment(R.id.nav_help)  // Puedes cambiar este fragmento por el que quieras
        }

        return binding.root
    }

    private fun initializeDefaultUsers() {
        val defaultUsers = listOf(
            Usuario(1, "Juan Pérez", "juan@example.com"),
            Usuario(2, "María Gómez", "maria@example.com"),
            Usuario(3, "Carlos López", "carlos@example.com"),
            Usuario(4, "Ana Fernández", "ana@example.com"),
            Usuario(5, "Pedro Sánchez", "pedro@example.com")
        )
        PreferencesHelper.saveUsuarios(requireContext(), defaultUsers)
    }

    private fun showAddUserDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_edit_user, null)
        val inputName = dialogView.findViewById<TextInputEditText>(R.id.etUserName)
        val inputEmail = dialogView.findViewById<TextInputEditText>(R.id.etUserEmail)

        MaterialAlertDialogBuilder(requireContext())
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
                    Toast.makeText(requireContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show()
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

        MaterialAlertDialogBuilder(requireContext())
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
                    Toast.makeText(requireContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show()
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
        PreferencesHelper.saveUsuarios(requireContext(), usuarios)
    }

    // Método para navegar a otros fragmentos
    private fun navigateToFragment(fragmentId: Int) {
        navController.navigate(fragmentId)
    }
}
