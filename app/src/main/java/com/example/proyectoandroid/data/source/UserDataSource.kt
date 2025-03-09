package com.example.proyectoandroid.data.source

import android.util.Log
import com.example.proyectoandroid.data.User


class UserDataSource {
    private val users = mutableListOf(
        User("12345678A", "Juan Pérez", "juan@example.com", "password1", "612345678", "https://picsum.photos/200/300", true),
        User("87654321B", "María Gómez", "maria@example.com", "password2", "623456789", "https://picsum.photos/200/300", false),
        User("23456789C", "Carlos López", "carlos@example.com", "password3", "634567890", "https://picsum.photos/200/300", true),
        User("34567890D", "Ana Fernández", "ana@example.com", "password4", "645678901", "https://picsum.photos/200/300", true),
        User("45678901E", "Pedro Sánchez", "pedro@example.com", "password5", "656789012", "https://picsum.photos/200/300", true),
        User("56789012F", "Laura Martínez", "laura@example.com", "password6", "667890123", "https://picsum.photos/200/300", true),
        User("67890123G", "Jorge Ruiz", "jorge@example.com", "password7", "678901234", "https://picsum.photos/200/300", true),
        User("78901234H", "Sofía Díaz", "sofia@example.com", "password8", "689012345", "https://picsum.photos/200/300", true),
        User("89012345I", "Miguel Torres", "miguel@example.com", "password9", "690123456", "https://picsum.photos/200/300", true),
        User("90123456J", "Elena Vargas", "elena@example.com", "password10", "601234567", "https://picsum.photos/200/300", true),
        User("01234567K", "Daniel Castro", "daniel@example.com", "password11", "612345678", "https://picsum.photos/200/300", true),
        User("12345678L", "Lucía Ramírez", "lucia@example.com", "password12", "623456789", "https://picsum.photos/200/300", true),
        User("23456789M", "Pablo Morales", "pablo@example.com", "password13", "634567890", "https://picsum.photos/200/300", true),
        User("34567890N", "Carmen Ortega", "carmen@example.com", "password14", "645678901", "https://picsum.photos/200/300", true),
        User("45678901O", "Alejandro Reyes", "alejandro@example.com", "password15", "656789012", "https://picsum.photos/200/300", true),
        User("56789012P", "Marta Jiménez", "marta@example.com", "password16", "667890123", "https://picsum.photos/200/300", true),
        User("67890123Q", "Raúl Navarro", "raul@example.com", "password17", "678901234", "https://picsum.photos/200/300", true),
        User("78901234R", "Isabel Molina", "isabel@example.com", "password18", "689012345", "https://picsum.photos/200/300", true),
        User("89012345S", "Fernando Gil", "fernando@example.com", "password19", "690123456", "https://picsum.photos/200/300", true),
        User("90123456T", "Patricia Rubio", "patricia@example.com", "password20", "601234567", "https://picsum.photos/200/300", true)
    )

    fun getUsers(): List<User> {
        return users.toList() // 🔹 Devolver una copia para evitar referencias incorrectas
    }

    fun delUser(userId: String) {
        Log.d("UserDataSource", "Intentando eliminar usuario con ID: $userId")
        val removed = users.removeAll { it.dni == userId }
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
        Log.d("UserDataSource", "Editando usuario: ${oldUser.name} -> ${newUser.name}")
        val i = users.indexOfFirst { it.email == newUser.email }
        if (i != -1) {
            users[i] = newUser
        }
    }
}