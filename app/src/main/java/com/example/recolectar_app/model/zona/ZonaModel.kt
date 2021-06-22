package com.example.recolectar_app.model.zona

import com.example.recolectar_app.model.contenedor.ContenedorModel
import java.io.Serializable


data class ZonaModel(var id: String) : Serializable {
    val type : String = "Zona"
    var refVehicle: RefVehicle? = null
    lateinit var nombre : Nombre
    lateinit var contenedores : Contenedores

    init {
        this.id = "zona:${id}"
    }

    fun setNombre(nombre : String){
        this.nombre = Nombre(nombre)
    }

    fun setContenedores(arr : ArrayList<ContenedorModel>){
        this.contenedores = Contenedores(arr)
    }


    fun setRefVehicleValue(string: String){
        this.refVehicle = RefVehicle("vehicle:${string}")
    }


    data class Nombre(var value : String){
        val type : String = "Text"
    }

    data class RefVehicle(var value : String){
        val type: String = "Relationship"
    }

    data class Contenedores(val value : ArrayList<ContenedorModel>){
        val type: String = "List"

        fun addContenedor(contenedorModel : ContenedorModel){
            value.add(contenedorModel)
        }
    }

    data class ZonaResponse(val code: Int?, val message: String?)



}

