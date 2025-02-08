package com.example.proyectoandroid.data.repository

import com.example.proyectoandroid.data.service.UserService
import com.example.proyectoandroid.data.source.UserDataSource
import com.example.proyectoandroid.domain.models.User
import com.example.proyectoandroid.domain.models.UserData
import com.example.proyectoandroid.domain.repository.UserRepositoryInterface

class InMemoryUserRepository : UserRepositoryInterface{
    private val service : UserService = UserService()

    override suspend fun getUsers() : List<User>{
        val mutableUsers : MutableList<User> = mutableListOf()
        val dataSource = service.getUsers()
        dataSource.forEach{
            userTriple -> mutableUsers.add(
                            User(userTriple.first, userTriple.second, userTriple.third))
        }
        UserData.users
        return UserData.users
    }

    override suspend fun getUsersByUsername(username: String): List<User> {
        val mutableUsers : MutableList<User> = mutableListOf()
        val dataSouce = service.getUserByUsername(username)
        dataSouce.forEach {
            userTriple -> mutableUsers.add(
                            User(userTriple.first, userTriple.second, userTriple.third))
        }
        UserData.users = mutableUsers
        return UserData.users
    }
}