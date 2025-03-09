package com.example.proyectoandroid.ui.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectoandroid.R
import com.example.proyectoandroid.databinding.ItemUsuarioBinding
import com.example.proyectoandroid.domain.models.User
import kotlin.random.Random

class ViewHolderUser(private val binding: ItemUsuarioBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        user: User,
        onEditClick: (User) -> Unit,
        onDeleteClick: (User) -> Unit
    ) {

        val numeroAleatorio = Random.nextInt(1, 1001)


        binding.tvNombre.text = user.name
        binding.tvEmail.text = user.email
        binding.tvDni.text = user.dni
        binding.tvPhone.text = user.phone


        // Si quieres que cada usuario tenga una imagen distinta, puedes generar una URL dinámica:
        val imageUrl = if (user.image?.startsWith("http") == true) {
            // Ejemplo usando picsum.photos con el id del usuario como semilla
            "https://picsum.photos/seed/${numeroAleatorio}/200/200"
        } else {
            // En caso de ser un recurso local, obtener el identificador
            val resId = binding.imageUser.context.resources.getIdentifier(
                user.image, "drawable", binding.imageUser.context.packageName
            )
            resId
        }

        if (user.disponible == true) {
            binding.tvDisponible.text = "Disponible"
            binding.tvDisponible.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.blue))
        } else {
            binding.tvDisponible.text = "No disponible"
            binding.tvDisponible.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.primaryColor))
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
