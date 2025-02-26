package com.example.proyectoandroid.data.repository

import android.util.Log
import com.example.proyectoandroid.data.service.UserService
import com.example.proyectoandroid.domain.models.User
import com.example.proyectoandroid.domain.repository.UserRepositoryInterface
import javax.inject.Inject

class InMemoryUserRepository @Inject constructor(
    private val userService: UserService
) : UserRepositoryInterface<User> {
    private val users = mutableListOf<User>()
    override suspend fun getUsers(): List<User> {
        users.clear()
        users.addAll(userService.getUsers())
        return users
    }

    override suspend fun delUser(id: Int): Boolean {
        Log.d("InMemoryUserRepository", "Intentando eliminar usuario con ID: $id")
        val result = userService.deleteUser(id)  // Llamamos directamente al servicio que trabaja con el DataSource
        Log.d("InMemoryUserRepository", "Usuario eliminado correctamente de memoria")
        return result
    }





    override suspend fun editUser(oldUser: User, newUser: User) {
        Log.d("InMemoryUserRepository", "Editando usuario: ${oldUser.nombre} -> ${newUser.nombre}")
        userService.editUser(oldUser, newUser)
    }

    override suspend fun addUser(o: User) {
        userService.addUser(o)
    }


}
