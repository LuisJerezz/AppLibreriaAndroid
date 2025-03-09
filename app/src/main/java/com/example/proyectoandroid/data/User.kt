package com.example.proyectoandroid.data

import com.google.gson.annotations.Expose
import java.io.Serializable

data class User (
    @Expose(serialize = false)
    var dni : String?,
    @Expose
    var name: String?,

    @Expose
    var email: String?,

    @Expose
    var password: String?,

    @Expose
    var phone: String?,

    @Expose
    var image: String?,

    @Expose
    var disponible: Boolean?
) : Serializable

data class AuthResponse(
    val token : String
)
