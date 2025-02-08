package com.example.proyectoandroid.domain.usecase

import com.example.proyectoandroid.data.repository.InMemoryUserRepository
import com.example.proyectoandroid.domain.models.User

class GetUsersUseCase(private val inMemoryUserRepository : InMemoryUserRepository) {
    suspend operator fun invoke() : List<User>?{
        return inMemoryUserRepository.getUsers()
    }
}