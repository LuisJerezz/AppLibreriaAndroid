package com.example.proyectoandroid.domain.usecase

import com.example.proyectoandroid.domain.models.User
import com.example.proyectoandroid.domain.repository.UserRepositoryInterface
import javax.inject.Inject

class EditUserUseCase @Inject constructor(private val repositoryInterface: UserRepositoryInterface<User>) {
    suspend operator fun invoke(oldUser: User, newUser: User){
        repositoryInterface.editUser(oldUser, newUser)
    }
}