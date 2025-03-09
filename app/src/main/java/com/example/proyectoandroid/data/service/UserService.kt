package com.example.proyectoandroid.data.service

import android.util.Log
import com.example.proyectoandroid.data.User
import com.example.proyectoandroid.data.source.UserDataSource



class UserService {
    private val dataSource = UserDataSource()

    suspend fun getUsers(): List<com.example.proyectoandroid.data.User> {
        return dataSource.getUsers()
    }

    suspend fun deleteUser(id: String): Boolean {
        Log.d("UserService", "Eliminando usuario con ID: $id en UserService")
        dataSource.delUser(id)
        return true
    }


    suspend fun editUser(oldUser: User, newUser: User) {
        Log.d("UserService", "Editando usuario: ${oldUser.name} -> ${newUser.name}")
        dataSource.editUser(oldUser, newUser)
    }

    suspend fun addUser(user: User) {
        dataSource.addUser(user)
    }
}