package com.example.proyectoandroid.domain.usecase

import com.example.proyectoandroid.domain.models.User
import com.example.proyectoandroid.domain.models.UserUpdateRequest
import com.example.proyectoandroid.domain.repository.UserRepositoryInterface
import javax.inject.Inject

class EditUserUseCase @Inject constructor(
    private val repository: UserRepositoryInterface
) {
    suspend fun execute(dni: String, updateRequest: UserUpdateRequest) {
        repository.editUser(dni, updateRequest)
    }
}