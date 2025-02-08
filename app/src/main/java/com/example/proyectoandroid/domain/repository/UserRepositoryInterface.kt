package com.example.proyectoandroid.domain.repository

import com.example.proyectoandroid.domain.models.User

interface UserRepositoryInterface {
    suspend fun getUsers() : List<User>
    suspend fun getUsersByUsername(username : String) : List<User>
}