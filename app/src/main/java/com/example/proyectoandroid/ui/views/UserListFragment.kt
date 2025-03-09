package com.example.proyectoandroid.ui.views.fragment


import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.proyectoandroid.R
import com.example.proyectoandroid.databinding.FragmentUsuarioBinding
import com.example.proyectoandroid.domain.models.User
import com.example.proyectoandroid.models.UserActionType
import com.example.proyectoandroid.ui.adapter.UserAdapter
import com.example.proyectoandroid.ui.modelview.UserViewModel
import com.example.proyectoandroid.ui.views.UserDialogFragmentCU
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserListFragment : Fragment(R.layout.fragment_usuario) {

    private lateinit var binding: FragmentUsuarioBinding
    private val viewModel: UserViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsuarioBinding.inflate(inflater, container, false)
        setupRecyclerView()
        observeViewModel()
        setupAddButton()
        return binding.root
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter(::onDeleteUser, ::onEditUser)
        binding.recyclerViewUsers.adapter = userAdapter
        binding.recyclerViewUsers.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupAddButton() {
        binding.btnAddUser.setOnClickListener {
            showAddUserDialog()
        }

    }
    private fun refreshList() {
        viewModel.getUsers()
    }
    private fun onDeleteUser(user: User) {
        user.dni?.let { viewModel.deleteUser(it) }
        Toast.makeText(requireContext(), "Usuario eliminado: ${user.name}", Toast.LENGTH_SHORT).show()
    }

    private fun onEditUser(user: User) {
        val dialog = UserDialogFragmentCU.newInstance(user)
        dialog.onUpdate = { updatedUser ->
            viewModel.editUser(user.dni!!, updatedUser)
            refreshList()
        }
        dialog.show(parentFragmentManager, "EditUserDialog")
    }


    private fun showAddUserDialog() {
        val dialog = UserDialogFragmentCU()

        dialog.onCreate = { newUser ->
            viewModel.addUser(newUser)
            refreshList()
        }

        dialog.show(parentFragmentManager, "AddUserDialog")
    }

    private fun observeViewModel() {
        viewModel.userLiveData.observe(viewLifecycleOwner) { users ->
            updateUserList(users)
        }
    }

    private fun updateUserList(users : List<User>){
        Log.d("UserListFragment", "Usuarios recibidos: ${users.size}")
        userAdapter.submitList(users)
    }

}