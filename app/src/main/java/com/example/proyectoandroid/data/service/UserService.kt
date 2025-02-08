package com.example.proyectoandroid.data.service

import com.example.proyectoandroid.data.source.UserDataSource
import kotlinx.coroutines.delay

class UserService {
    suspend fun getUsers(): List<Triple<String, String, String>>{
        delay(1000);
        return UserDataSource.users
    }

    suspend fun getUserByUsername(username: String): List<Triple<String, String, String>>{
        delay(1000);
        val userByName = UserDataSource.users.filter {
            it.second.equals(username)
        }
        return userByName;
    }
}