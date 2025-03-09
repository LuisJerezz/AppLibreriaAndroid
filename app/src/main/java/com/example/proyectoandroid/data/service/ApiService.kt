package com.example.proyectoandroid.data.service


import com.example.proyectoandroid.data.AuthResponse
import com.example.proyectoandroid.data.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.*

interface ApiService {

    @GET("users")
    suspend fun getUsers(): Response<List<User>>

    @POST("users")
    suspend fun addUser(@Body user: User): Response<Unit>

    @PATCH("users/{id}")
    suspend fun editUser(@Path("id") id:String, @Body user: User) : Response<Unit>

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: String) : Response<Unit>

    @POST("register")
    fun register(@Body user: User): Call<Void>

    @POST("auth/login")
    fun login(@Body loginRequest : LoginRequest) : Call<AuthResponse>
}

data class LoginRequest(
    val email: String,
    val password: String
)