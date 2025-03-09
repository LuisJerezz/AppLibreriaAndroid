package com.example.proyectoandroid.domain.models

data class UserUpdateDto(
    val name: String,
    val email: String,
    val password: String,
    val phone: String,
    val image: String?,
    val disponible: Boolean?
)