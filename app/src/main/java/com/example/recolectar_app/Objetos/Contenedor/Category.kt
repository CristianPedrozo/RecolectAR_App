package com.example.recolectar_app.Objetos.Contenedor

data class Category(
        val metadata: Metadata,
        val type: String,
        val value: List<String>
)