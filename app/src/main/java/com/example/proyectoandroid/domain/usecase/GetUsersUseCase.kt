package com.example.proyectoandroid.domain.usecase

import com.example.proyectoandroid.domain.models.User
import com.example.proyectoandroid.domain.repository.UserRepositoryInterface
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(private val repository : UserRepositoryInterface) {
    suspend fun execute() : List<User>{
        return repository.getUsers()
    }
}