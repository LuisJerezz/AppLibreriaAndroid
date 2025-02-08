package com.example.proyectoandroid.data.source

object UserDataSource {
    val users : List<Triple<String, String, String>> = listOf(
        Triple("https://picsum.photos/200/300", "Juan Pérez", "juan@example.com"),
        Triple("https://picsum.photos/200/300", "María Gómez", "maria@example.com"),
        Triple("https://picsum.photos/200/300", "Carlos López", "carlos@example.com"),
        Triple("https://picsum.photos/200/300", "Ana Fernández", "ana@example.com"),
        Triple("https://picsum.photos/200/300", "Pedro Sánchez", "pedro@example.com")
    )
}