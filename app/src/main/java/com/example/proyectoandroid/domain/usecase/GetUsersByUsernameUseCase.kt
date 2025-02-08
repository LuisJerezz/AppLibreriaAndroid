package com.example.proyectoandroid.domain.usecase

import com.example.proyectoandroid.data.repository.InMemoryUserRepository
import com.example.proyectoandroid.domain.models.User

class GetUsersByUsernameUseCase(val inMemoryUserRepository: InMemoryUserRepository) {
    lateinit var username : String
    suspend operator fun invoke() : List<User>{
        return inMemoryUserRepository.getUsersByUsername(username)
    }
}