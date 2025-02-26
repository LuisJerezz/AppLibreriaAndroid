package com.example.proyectoandroid.data

import java.io.Serializable

data class User (
    var id : Int,
    var nombre : String,
    var email : String,
    var imagen : String
) : Serializable
