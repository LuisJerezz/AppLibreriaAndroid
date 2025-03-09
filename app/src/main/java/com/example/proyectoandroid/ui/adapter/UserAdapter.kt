package com.example.proyectoandroid.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectoandroid.R
import com.example.proyectoandroid.databinding.ItemUsuarioBinding
import com.example.proyectoandroid.domain.models.User


class UserAdapter(
    private val onDeleteClick: (User) -> Unit,
    private val onEditClick: (User) -> Unit
) : ListAdapter<User, UserAdapter.UserViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUsuarioBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class UserViewHolder(private val binding: ItemUsuarioBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.apply {
                tvNombre.text = user.name ?: "Sin nombre"
                tvEmail.text = user.email ?: "Sin email"
                tvPhone.text = user.phone?.toString() ?: "Sin teléfono"
                tvDni.text = user.dni ?: "Sin DNI"

                // Carga de imagen mejorada
                val imageUrl = when {
                    user.image.isNullOrEmpty() -> R.drawable.ic_users
                    user.image!!.startsWith("http") -> user.image
                    else -> {
                        val resId = root.context.resources.getIdentifier(
                            user.image, "drawable", root.context.packageName
                        )
                        if (resId == 0) R.drawable.ic_users else resId
                    }
                }

                Glide.with(root.context)
                    .load(imageUrl)
                    .error(R.drawable.ic_users)
                    .into(imageUser)

                btnEditar.setOnClickListener { onEditClick(user) }
                btnEliminar.setOnClickListener { onDeleteClick(user) }
            }
        }
    }

    class UserDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.dni == newItem.dni
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}