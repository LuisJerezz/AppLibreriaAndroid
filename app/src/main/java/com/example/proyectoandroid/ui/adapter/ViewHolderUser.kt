package com.example.proyectoandroid.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.proyectoandroid.databinding.ItemUserBinding
import com.example.proyectoandroid.domain.models.User

class ViewHolderUser(view: View) : RecyclerView.ViewHolder(view){
    private lateinit var binding: ItemUserBinding

    init {
        binding = ItemUserBinding.bind(view)
    }

    fun rendereize(getUser : User){
        Glide
            .with(itemView.context)
            .load(getUser.imagen)
            .centerCrop()
            .into(binding.imageView)
    }
}