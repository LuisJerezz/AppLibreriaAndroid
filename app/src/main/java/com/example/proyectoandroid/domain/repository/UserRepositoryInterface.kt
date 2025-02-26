package com.example.proyectoandroid.domain.repository

import com.example.proyectoandroid.domain.models.User

interface UserRepositoryInterface<T> {
    suspend fun getUsers() : List<User>
    suspend fun delUser(id:Int):Boolean
    suspend fun addUser(o:T)
    suspend fun editUser(oldUser:T, newUser: T)

}