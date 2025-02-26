package com.example.proyectoandroid.domain.models

import org.jetbrains.annotations.NotNull

data class User(
    var id: Int,
    var nombre: String,
    var email: String,
    val imagen: String
)
