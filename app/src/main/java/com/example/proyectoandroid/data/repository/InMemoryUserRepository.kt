package com.example.proyectoandroid.data.repository

import com.example.proyectoandroid.data.User as DataUser
import com.example.proyectoandroid.data.service.ApiService
import com.example.proyectoandroid.domain.models.UserCreateRequest
import com.example.proyectoandroid.domain.models.UserUpdateRequest
import com.example.proyectoandroid.domain.models.User as DomainUser
import com.example.proyectoandroid.domain.repository.UserRepositoryInterface
import javax.inject.Inject

class InMemoryUserRepository @Inject constructor(
    private val apiService: ApiService
) : UserRepositoryInterface {

    override suspend fun getUsers(): List<DomainUser> {
        val response = apiService.getUsers()
        return if (response.isSuccessful) {
            val dataUsers = response.body() ?: emptyList()
            dataUsers.map { it.toDomainUser() } // Convertir cada User de datos a User de dominio
        } else {
            emptyList()
        }
    }

    override suspend fun delUser(dni: String): Boolean {
        val response = apiService.deleteUser(dni)
        return response.isSuccessful
    }

    override suspend fun addUser(user: UserCreateRequest): Boolean {
        val dataUser = user.toDataUser()
        val response = apiService.addUser(dataUser)
        return response.isSuccessful
    }

    override suspend fun editUser(dni: String, user: UserUpdateRequest?): Boolean {
        if (user == null){
            return false
        }
        val dataUser = user.toDataUser()
        val response = apiService.editUser(dni, dataUser)
        return response.isSuccessful
    }



}

// Función de extensión para convertir User de datos a User de dominio
private fun DataUser.toDomainUser(): DomainUser {
    return DomainUser(
        dni = this.dni,
        name = this.name,
        email = this.email,
        password = this.password,
        phone = this.phone,
        image = this.image,
        disponible = this.disponible
    )
}


private fun DomainUser.toDataUser() : DataUser{
    return DataUser(
        dni = this.dni,
        name = this.name,
        email = this.email,
        password = this.password,
        phone = this.phone,
        image = this.image,
        disponible = this.disponible
    )
}

private fun UserUpdateRequest.toDataUser(): DataUser {
    return DataUser(
        dni = null,
        name = this.name,
        email = this.email,
        password = this.password,
        phone = this.phone,
        image = this.image ?: "default_avatar",
        disponible = this.disponible ?: true
    )
}

private fun UserCreateRequest.toDataUser(): DataUser {
    return DataUser(
        dni = this.dni,  // Mantenemos el DNI en creaciones
        name = this.name,
        email = this.email,
        password = this.password,
        phone = this.phone,
        image = this.image ?: "default_avatar",
        disponible = true  // Valor por defecto al crear
    )
}

