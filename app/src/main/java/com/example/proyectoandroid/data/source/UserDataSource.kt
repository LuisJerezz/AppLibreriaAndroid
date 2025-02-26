package com.example.proyectoandroid.data.source

import android.util.Log
import com.example.proyectoandroid.domain.models.User


class UserDataSource {
    private val users = mutableListOf<User>(
        User(1, "Juan Pérez", "juan@example.com", "https://picsum.photos/200/300"),
        User(2, "María Gómez", "maria@example.com", "https://picsum.photos/200/300"),
        User(3, "Carlos López", "carlos@example.com", "https://picsum.photos/200/300"),
        User(4, "Ana Fernández", "ana@example.com", "https://picsum.photos/200/300"),
        User(5, "Pedro Sánchez", "pedro@example.com", "https://picsum.photos/200/300"),
        User(6, "Laura Martínez", "laura@example.com", "https://picsum.photos/200/300"),
        User(7, "Jorge Ruiz", "jorge@example.com", "https://picsum.photos/200/300"),
        User(8, "Sofía Díaz", "sofia@example.com", "https://picsum.photos/200/300"),
        User(9, "Miguel Torres", "miguel@example.com", "https://picsum.photos/200/300"),
        User(10, "Elena Vargas", "elena@example.com", "https://picsum.photos/200/300"),
        User(11, "Daniel Castro", "daniel@example.com", "https://picsum.photos/200/300"),
        User(12, "Lucía Ramírez", "lucia@example.com", "https://picsum.photos/200/300"),
        User(13, "Pablo Morales", "pablo@example.com", "https://picsum.photos/200/300"),
        User(14, "Carmen Ortega", "carmen@example.com", "https://picsum.photos/200/300"),
        User(15, "Alejandro Reyes", "alejandro@example.com", "https://picsum.photos/200/300")
    )

    fun getUsers(): List<User> {
        return users.toList() // 🔹 Devolver una copia para evitar referencias incorrectas
    }

    fun delUser(userId: Int) {
        Log.d("UserDataSource", "Intentando eliminar usuario con ID: $userId")
        val removed = users.removeAll { it.id == userId }
        if (removed) {
            Log.d("UserDataSource", "Usuario eliminado correctamente")
        } else {
            Log.e("UserDataSource", "Usuario con ID $userId no encontrado")
        }
    }

    fun addUser(user: User) {
        users.add(user)
    }

    fun editUser(oldUser: User, newUser: User) {
        Log.d("UserDataSource", "Editando usuario: ${oldUser.nombre} -> ${newUser.nombre}")
        val i = users.indexOfFirst { it.email == newUser.email }
        if (i != -1) {
            users[i] = newUser
        }
    }
}