package com.example.proyectoandroid.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoandroid.databinding.ItemUsuarioBinding
import com.example.proyectoandroid.domain.models.User


class UserAdapter(
    private var users: MutableList<User> = mutableListOf(),
    private val onEditClick: (User) -> Unit,
    private val onDeleteClick: (User) -> Unit
) : RecyclerView.Adapter<ViewHolderUser>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderUser {
        val binding = ItemUsuarioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderUser(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderUser, position: Int) {
        val user = users[position]
        holder.bind(user, { userToEdit ->
            Log.d("UserAdapter", "Llamando a onEditClick para usuario: ${userToEdit.nombre}") // 🚀 Verifica que el click llega al adapter
            onEditClick(userToEdit)
        }, onDeleteClick)
    }

    override fun getItemCount(): Int = users.size

    fun updateList(newList: List<User>) {
        users.clear()
        users.addAll(newList)
        notifyDataSetChanged()
    }


}
