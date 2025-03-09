package com.example.proyectoandroid.domain.usecase

import com.example.proyectoandroid.domain.models.User
import com.example.proyectoandroid.domain.models.UserCreateRequest
import com.example.proyectoandroid.domain.repository.UserRepositoryInterface
import javax.inject.Inject

class AddUserUseCase @Inject constructor(
    private val repository: UserRepositoryInterface
) {
    suspend fun execute(request: UserCreateRequest) {
        repository.addUser(request)
    }
}