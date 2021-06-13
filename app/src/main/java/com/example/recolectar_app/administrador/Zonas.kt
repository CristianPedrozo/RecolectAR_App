package com.example.recolectar_app.administrador

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.recolectar_app.R
import com.example.recolectar_app.RequestHandler
import com.example.recolectar_app.adapters.ZonaListAdapter
import com.example.recolectar_app.zonas.Zona
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson


class Zonas : Fragment() {
    var url = "http://46.17.108.122:1026/v2/entities/?type=Zona"
    lateinit var thiscontext : Context
    lateinit var v: View
    lateinit var botton_agregar: FloatingActionButton
    lateinit var recZonas : RecyclerView
    var zonas : MutableList<Zona> = ArrayList()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var zonaListAdapter: ZonaListAdapter

    companion object {
        fun newInstance() = Zonas()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (container != null) {
            thiscontext = container.context
        };

        v =  inflater.inflate(R.layout.fragment_list_zonas, container, false)
        recZonas = v.findViewById(R.id.rec_zonas)
        var requestHandler = RequestHandler.getInstance(thiscontext)
        getData(requestHandler)
        botton_agregar = v.findViewById(R.id.btn_agregar_zona)
        botton_agregar.setOnClickListener() {
            val action = ZonasDirections.actionZonasToAltaZona()
            v.findNavController().navigate(action)
        }
        return v
    }


    fun getData(requestHandler:RequestHandler){
        val gson = Gson()
        requestHandler.getArrayRequest(url,
            { response ->
                for (i in 0 until response.length()) {
                    val zona : Zona = gson.fromJson(response.getJSONObject(i).toString(),Zona::class.java)
                    zonas.add(zona)
                }
                recZonas.setHasFixedSize(true)
                linearLayoutManager = LinearLayoutManager(context)
                recZonas.layoutManager = linearLayoutManager

                zonaListAdapter = ZonaListAdapter(zonas);

                recZonas.adapter = zonaListAdapter
                zonaListAdapter.setData(zonas as ArrayList<Zona>)
            },
            { error ->
                Toast.makeText(this@Zonas.requireContext(), "error" + error, Toast.LENGTH_LONG).show()
            }
        ,null)
    }


}