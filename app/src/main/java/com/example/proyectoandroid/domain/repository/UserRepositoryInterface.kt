package com.example.proyectoandroid.domain.repository

import com.example.proyectoandroid.domain.models.User
import com.example.proyectoandroid.domain.models.UserCreateRequest
import com.example.proyectoandroid.domain.models.UserUpdateRequest

interface UserRepositoryInterface {
    suspend fun getUsers() : List<User>
    suspend fun delUser(dni : String):Boolean
    suspend fun addUser(user : UserCreateRequest): Boolean
    suspend fun editUser(dni : String, user: UserUpdateRequest?) : Boolean

}