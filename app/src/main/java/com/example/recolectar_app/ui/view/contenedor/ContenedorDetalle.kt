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
import com.example.recolectar_app.databinding.FragmentContenedorDetalleBinding
import com.example.recolectar_app.model.DeleteRequestModel
import com.example.recolectar_app.model.contenedor.ContenedorModel
import com.example.recolectar_app.ui.viewModel.contenedor.ContenedorDetalleVM

class ContenedorDetalle : Fragment() {
    private val TAG = "ContenedorDetalle"
    private var _binding: FragmentContenedorDetalleBinding? = null
    private val binding get() = _binding!!
    private val contenedorDetalleVM : ContenedorDetalleVM by viewModels()
    private lateinit var contenedorModel: ContenedorModel
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
        _binding = FragmentContenedorDetalleBinding.inflate(layoutInflater,container,false)
        if (container != null) {
            thiscontext = container.context
        };
        val args = arguments?.let { ContenedorDetalleArgs.fromBundle(it) }
        contenedorModel = args?.contenedor!!

        binding.textId.setText(contenedorModel.id.split(":")[1])
        binding.textTipo.setText(contenedorModel.wasteType.value)
        binding.textLatitud.setText(contenedorModel.location.value.coordinates[0].toString())
        binding.textLongitud.setText(contenedorModel.location.value.coordinates[1].toString())
        binding.textEstado.setText(contenedorModel.status.value)
        binding.textRuta.setText(contenedorModel.refRuta?.value)
        binding.textCamion.setText(contenedorModel.refVehicle?.value ?: "")
        binding.textTemperatura.setText(contenedorModel.temperature?.value.toString())
        binding.textZona.setText(contenedorModel.refZona.value.split(":")[1])
        binding.textProximaVisita.setText(contenedorModel.nextActuationDeadline?.value)
        binding.textUltimaVisita.setText(contenedorModel.dateLastEmptying?.value)
        binding.textLlenado.setText(contenedorModel.fillingLevel.value.toString())

        binding.botonEditarContenedor.setOnClickListener(){
            val action = ContenedorDetalleDirections.actionContenedorDetalleToUpdateContenedor(contenedorModel)
            binding.root.findNavController().navigate(action)
        }

        binding.botonRemoverContenedor.setOnClickListener(){
            removeContenedor()
            val action = ContenedorDetalleDirections.actionContenedorDetalleToContenedores()
            binding.root.findNavController().navigate(action)
        }
        
        contenedorDetalleVM.deleteContenedorResult.observe(viewLifecycleOwner,{ result ->
            if(result){
                Toast.makeText(thiscontext, "EXITO", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(thiscontext, "FAIL", Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }

    private fun removeContenedor() {
        val contenedor = ContenedorModel(contenedorModel.id.split(":")[1])
        val contenedorDeleteRequest = DeleteRequestModel()
        contenedorDeleteRequest.addContenedor(contenedor)
        contenedorDetalleVM.deleteContenedor(contenedorDeleteRequest)
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            ContenedorDetalle().apply {
                arguments = Bundle().apply {

                }
            }
    }
}