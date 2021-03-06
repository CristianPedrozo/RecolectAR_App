package com.example.recolectar_app.empleado

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import com.example.recolectar_app.Usuario
import com.example.recolectar_app.administrador.Datos_AdministradorDirections
import com.example.recolectar_app.databinding.FragmentEmpleadoDatosBinding
import com.example.recolectar_app.databinding.FragmentUsuariosDatosBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class Datos : Fragment() {
    private var _binding: FragmentEmpleadoDatosBinding? = null
    private var _bindingUsr : FragmentUsuariosDatosBinding? = null
    private val binding get() = _binding!!
    private val bindingUsr get() = _bindingUsr!!
    private lateinit var datos : TextView
    private lateinit var auth: FirebaseAuth
    var estadoActualUsuario:Boolean? = false
    val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmpleadoDatosBinding.inflate(layoutInflater,container,false)

        // Initialize Firebase Auth
        binding.btnAgregarUsuario.setOnClickListener {
            agregarUsuario()
        }
        // Initialize Firebase Auth
        auth = Firebase.auth
        val email = getUserInstance()
        if(email != null)
            obtenerUsuarioFirebase(email.toString())

        return binding.root

    }


    fun obtenerDatos(): Usuario {
        val razonSocial = binding.editTextRazonSocial.text.toString()
        val usuario = binding.editTextUsuario .text.toString()
        val zona = binding.editTextZona.text.toString()
        val email = binding.editTextEmail.text.toString()
        val horarioEntrada = binding.editTextHorarioEntrada.text.toString()
        val horarioSalida = binding.editTextHorarioSalida.text.toString()
        return Usuario(razonSocial, usuario ,email, zona,horarioEntrada,horarioSalida,false,estadoActualUsuario,"")
    }

    fun validarDatos(usuario: Usuario):Boolean{
        val contrasenia1 = binding.editTextContraseAEmpleado1.text.toString()
        val contrasenia2 = binding.editTextContraseAEmpleado2.text.toString()

        if(contrasenia1 != contrasenia2)
            return false
        if(usuario.razonSocial.toString().isEmpty())
            return false
        if(usuario.zona.toString().isEmpty())
            return false
        if(usuario.horarioEntrada.toString().isEmpty())
            return false
        if(usuario.horarioSalida.toString().isEmpty())
            return false
        if(usuario.usuario.isEmpty())
            return false

        return true
    }

    fun guardarUsuarioFirebase(usuario: Usuario){
        db.collection("usuarios").document(usuario.usuario).set(
            hashMapOf(
                "zona" to usuario.zona,
                "esAdmin" to  usuario.esAdmin,
                "horarioEntrada" to usuario.horarioEntrada,
                "horarioSalida" to usuario.horarioSalida,
                "email" to usuario.email,
                "estaActivo" to estadoActualUsuario,
                "razonSocial" to usuario.razonSocial
            )
        ).addOnSuccessListener{
            /* if(email != null){
                 Toast.makeText(this, "Usuario editado con exito", Toast.LENGTH_SHORT).show()
             }else{
                 Toast.makeText(this, "Usuario agregado con exito", Toast.LENGTH_SHORT).show()
             }*/
        }.addOnFailureListener{
            /* if(email != null){
                 Toast.makeText(this, "Error al editar el usuario", Toast.LENGTH_SHORT).show()
             }else{
                 Toast.makeText(this, "Error al agregar el usuario", Toast.LENGTH_SHORT).show()
             }*/
        }
    }

    fun agregarUsuario(){
        val usuario = Usuario(
            binding.editTextRazonSocial.text.toString(),
            binding.editTextUsuario.text.toString(),
            binding.editTextEmail.text.toString(),
            binding.editTextZona.text.toString(),
            binding.editTextHorarioEntrada.text.toString(),
            binding.editTextHorarioSalida.text.toString(),
            false,
            estadoActualUsuario,
            "")

        if(validarDatos(usuario)){
            usuario.usuario = asignaroCompletarUsuario(usuario.usuario)
            guardarUsuarioFirebase(usuario)
            actualizarPassword()
        }
        else{
            Toast.makeText(binding.root.context,"Datos invalidos, no se pudo guardar el usuario", Toast.LENGTH_LONG ).show()
        }
    }

    fun asignaroCompletarUsuario(usuario: String):String{
        var usuarioAsigned = ""
        if(!usuario.contains("@")){
            usuarioAsigned = "$usuario@fiware.com.ar"
        }
        else{
            usuarioAsigned = usuario
        }
        return usuarioAsigned
    }

    fun actualizarPassword(){
        val contrasenia1 = binding.editTextContraseAEmpleado1.text.toString()
        val contrasenia2 = binding.editTextContraseAEmpleado2.text.toString()

        if(contrasenia1 != "" && contrasenia2 != "") {
            val user = Firebase.auth.currentUser

            user!!.updatePassword(contrasenia1)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                    }
                }
        }
    }

    fun obtenerUsuarioFirebase(usuario:String?){
        if(usuario != null){
            usuario.toString()
            db.collection("usuarios").document(usuario)
                .get()
                .addOnSuccessListener {document->
                    val usuario = Usuario(
                        document.getString("razonSocial"),
                        usuario,
                        document.getString("email"),
                        document.getString("zona"),
                        document.getString("horarioEntrada"),
                        document.getString("horarioSalida"),
                        document.getBoolean("esAdmin"),
                        document.getBoolean("estaActivo"),
                        "https://www.uclg-planning.org/sites/default/files/styles/featured_home_left/public/no-user-image-square.jpg?itok=PANMBJF-")
                    estadoActualUsuario = usuario.estaActivo
                    cargarCampos(usuario)
                }
        }
    }

    fun redireccionarAUsuarios(){
        val action = Datos_AdministradorDirections.datosToUsuario()
        binding.root.findNavController().navigate(action)
    }

    fun cargarCampos(usuario : Usuario){
        binding.editTextRazonSocial.setText(usuario.razonSocial)
        var usuarioLimpio= ""
        var index = usuario.usuario.indexOf('@')
        if(index > 0 && usuario.usuario.contains("fiware"))
            usuarioLimpio = usuario.usuario.substring(0,index)
        else
            usuarioLimpio = usuario.usuario

        binding.editTextEmail.setText(usuario.email)
        binding.editTextZona.setText(usuario.zona)
        binding.editTextUsuario.setText(usuarioLimpio)
        binding.editTextHorarioEntrada.setText(usuario.horarioEntrada)
        binding.editTextHorarioSalida.setText(usuario.horarioSalida)
    }

    fun getUserInstance(): String?{
        var email:String? = null
        val user : FirebaseUser? = FirebaseAuth.getInstance().currentUser
        if (user != null)
            email = user.email

        return email
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String) =
            Datos().apply {
                arguments = Bundle().apply {

                }
            }
    }


}