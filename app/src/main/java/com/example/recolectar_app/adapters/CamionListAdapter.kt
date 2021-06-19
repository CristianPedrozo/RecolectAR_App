package com.example.recolectar_app.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recolectar_app.R
import com.example.recolectar_app.camiones.Camion
import com.example.recolectar_app.holders.CamionHolder

class CamionListAdapter ( private var camionList: MutableList<Camion>) : RecyclerView.Adapter<CamionHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CamionHolder {
        val layoutInflater =  LayoutInflater.from(parent.context)
        return CamionHolder(layoutInflater.inflate(R.layout.fragment_item_camion,parent,false))
    }

    override fun getItemCount(): Int {

        return camionList.size
    }

    fun setData(newData: ArrayList<Camion>) {
        this.camionList = newData
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CamionHolder, position: Int) {
        holder.bind(camionList[position])
    }
}