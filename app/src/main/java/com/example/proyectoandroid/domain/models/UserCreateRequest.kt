package com.example.proyectoandroid.domain.models

data class UserCreateRequest(
    val dni: String,
    val name: String,
    val email: String,
    val password: String,
    val phone: String,
    val image: String?
)