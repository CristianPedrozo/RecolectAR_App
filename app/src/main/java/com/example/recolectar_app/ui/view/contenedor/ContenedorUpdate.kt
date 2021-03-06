package com.example.recolectar_app.ui.view.contenedor

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.recolectar_app.databinding.FragmentUpdateContenedorBinding
import com.example.recolectar_app.model.UpdateRequestModel
import com.example.recolectar_app.model.contenedor.ContenedorModel
import com.example.recolectar_app.ui.view.camion.CamionUpdateDirections
import com.example.recolectar_app.ui.viewModel.contenedor.ContenedorUpdateVM
import com.google.gson.Gson


class ContenedorUpdate : Fragment() {
    private val TAG = "Update Contenedor"
    private var _binding: FragmentUpdateContenedorBinding? = null
    private val binding get() = _binding!!
    private val contenedorUpdateVM : ContenedorUpdateVM  by viewModels()
    private lateinit var contenedor: ContenedorModel
    lateinit var thiscontext : Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateContenedorBinding.inflate(layoutInflater,container,false)

        if (container != null) {
            thiscontext = container.context
        }
        val args = arguments?.let { ContenedorDetalleArgs.fromBundle(it) }
        contenedor = args?.contenedor!!
        loadContenedorData(contenedor.id)

        
        contenedorUpdateVM.contenedorUpdateResult.observe(viewLifecycleOwner, { result ->
            if(result){
                Toast.makeText(thiscontext, "EXITO", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(thiscontext, "FAIL", Toast.LENGTH_SHORT).show()
            }
        })
        
        binding.botonConfirmarEditarContenedor.setOnClickListener(){
            editContenedor()
            val action = ContenedorUpdateDirections.actionUpdateContenedorToContenedores()
            binding.root.findNavController().navigate(action)
        }

        binding.botonCancelarEdit.setOnClickListener(){
            val action = ContenedorUpdateDirections.actionUpdateContenedorToContenedores()
            binding.root.findNavController().navigate(action)
        }
        return binding.root
    }

    private fun loadContenedorData(contenedorId: String) = with(binding){
        contenedorUpdateVM.contenedorSelectedData.observe(viewLifecycleOwner,{data->
            edittextContenedorId.setText(data.id.split(":")[1])
            edittextContenedorLongitud.setText(data.location.value.coordinates[1].toString())
            edittextContenedorLatitud.setText(data.location.value.coordinates[0].toString())
            edittextContenedorProximaVisita.setText(data.nextActuationDeadline?.value ?: "")
            edittextContenedorUltimaVisita.setText(data.dateLastEmptying?.value ?: "")
            edittextContenedorLlenado.setText(data.fillingLevel.value.toString())
//            edittextContenedorTemperatura.setText(data.temperature?.value?.toString() ?: "")
            edittextContenedorZona.setText(data.refZona.value.split(":")[1])
//            edittextContenedorRuta.setText(data.refRuta?.value ?: "")
            edittextContenedorEstado.setText(data.status.value)
            edittextContenedorCamion.setText(data.refVehicle?.value ?: "")
        })
        val string = "?id=$contenedorId"
        contenedorUpdateVM.getContenedorById(string)
    }

    private fun editContenedor() = with(binding) {
        val contenedor = ContenedorModel(edittextContenedorId.text.toString())
        contenedor.setRefZona(edittextContenedorZona.text.toString())
        contenedor.setStatus(edittextContenedorEstado.text.toString())
        contenedor.setFillingLevel(edittextContenedorLlenado.text.toString().toDouble())
        contenedor.setDateLastEmptying(edittextContenedorUltimaVisita.text.toString())
//        contenedor.setNextActuationDeadLine(edittextContenedorProximaVisita.text.toString())
//        contenedor.setRefRuta(edittextContenedorRuta.text.toString())
        contenedor.setRefVehicle(edittextContenedorCamion.text.toString())
//        contenedor.setTemperature(edittextContenedorTemperatura.text.toString().toDouble())
//        contenedor.setWasteType(edittextContenedorTipo.text.toString())
        contenedor.setLocation(mutableListOf(edittextContenedorLatitud.text.toString().toDouble(),edittextContenedorLongitud.text.toString().toDouble()))
        val contenedorUpdateRequest = UpdateRequestModel()
        contenedorUpdateRequest.addContenedor(contenedor)
//        val gson = Gson()
//        val asd = gson.toJson(contenedorUpdateRequest)
//        Toast.makeText(thiscontext, asd, Toast.LENGTH_SHORT).show()
        contenedorUpdateVM.updateContenedor(contenedorUpdateRequest)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ContenedorUpdate().apply {
                arguments = Bundle().apply {

                }
            }
    }

}