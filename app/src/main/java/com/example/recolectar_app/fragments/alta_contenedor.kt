package com.example.recolectar_app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import com.example.recolectar_app.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class alta_contenedor : Fragment() {
    lateinit var v:View
    lateinit var codigo:TextInputLayout
    lateinit var autoCompleteTextView: AutoCompleteTextView
    lateinit var autoCompleteTextView_tipo: AutoCompleteTextView
    lateinit var botonCrear: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v= inflater.inflate(R.layout.fragment_alta_contenedor, container, false)
        //Carga Combo Estados
        val estados = resources.getStringArray(R.array.estados_contenedor)
        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.combo_formulario,estados)
        autoCompleteTextView = v.findViewById(R.id.autoCompleteTextView)
        autoCompleteTextView.setAdapter(arrayAdapter)

        //Carga Combo Tipo
        val tipos = resources.getStringArray(R.array.tipos_residuos)
        val arrayAdapterTipo = ArrayAdapter(requireContext(),R.layout.combo_formulario,tipos)
        autoCompleteTextView_tipo = v.findViewById(R.id.autoCompleteTextView_tipo)
        autoCompleteTextView_tipo.setAdapter(arrayAdapterTipo)

        //Validar campos para el alta de un contenedor
        botonCrear=v.findViewById(R.id.boton_agregar)
        botonCrear.setOnClickListener{validarCampos()}

        return v
    }

    private fun validarCampos(){
        val result = arrayOf(validarCodigo())
        if(false in result){
            return
        }
    }

    private fun validarCodigo():Boolean {
        codigo = v.findViewById(R.id.editText_Codigo)
        return if (!codigo.toString().isEmpty()) {
            codigo.error = "El campo es requerido"
            false
        }else{
            true
        }
    }

}