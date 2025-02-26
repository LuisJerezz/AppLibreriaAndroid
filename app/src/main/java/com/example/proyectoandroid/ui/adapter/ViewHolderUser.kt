package com.example.proyectoandroid.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectoandroid.databinding.ItemUsuarioBinding
import com.example.proyectoandroid.domain.models.User

class ViewHolderUser(private val binding: ItemUsuarioBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        user: User,
        onEditClick: (User) -> Unit,
        onDeleteClick: (User) -> Unit
    ) {
        binding.tvNombre.text = user.nombre
        binding.tvEmail.text = user.email

        // Si quieres que cada usuario tenga una imagen distinta, puedes generar una URL dinámica:
        val imageUrl = if (user.imagen.startsWith("http")) {
            // Ejemplo usando picsum.photos con el id del usuario como semilla
            "https://picsum.photos/seed/${user.id}/200/200"
        } else {
            // En caso de ser un recurso local, obtener el identificador
            val resId = binding.imageUser.context.resources.getIdentifier(
                user.imagen, "drawable", binding.imageUser.context.packageName
            )
            resId
        }

        // Carga la imagen (la sobrecarga de load acepta Int o String)
        Glide.with(itemView.context)
            .load(imageUrl)
            .centerCrop()
            .into(binding.imageUser)

        binding.btnEliminar.setOnClickListener {
            onDeleteClick(user)
        }

        binding.btnEditar.setOnClickListener {
            onEditClick(user)
        }
    }
}
