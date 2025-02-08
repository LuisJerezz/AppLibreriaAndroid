package com.example.proyectoandroid.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoandroid.R
import com.example.proyectoandroid.domain.models.User
import com.example.proyectoandroid.domain.models.UserData

class UserAdapter : RecyclerView.Adapter<ViewHolderUser>() {

    var userRepository : List<User> = UserData.users

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderUser {
        var layoutInflater = LayoutInflater.from(parent.context)
        var layoutUserItem = R.layout.item_user
        return ViewHolderUser(
            layoutInflater.inflate(layoutUserItem, parent, false)
        )
    }

    override fun getItemCount() = userRepository.size


    override fun onBindViewHolder(holder: ViewHolderUser, position: Int) {
        holder.rendereize(userRepository.get(position))
    }

}