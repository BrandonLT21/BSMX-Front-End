package com.example.bsmx

import Transaccion
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransaccionAdapter (private val transacciones: List<Transaccion>) :
    RecyclerView.Adapter<TransaccionAdapter.TransaccionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransaccionViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_transaccion, parent, false)
        return TransaccionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TransaccionViewHolder, position: Int) {
        val currentTransaccion = transacciones[position]
        holder.textFecha.text = currentTransaccion.fecha
        holder.textDescripcion.text = currentTransaccion.descripcion
        holder.textMonto.text = currentTransaccion.monto.toString()
        holder.textDetalle.text = currentTransaccion.detalle
    }

    override fun getItemCount(): Int {
        return transacciones.size
    }

    class TransaccionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textFecha: TextView = itemView.findViewById(R.id.textFecha)
        val textDescripcion: TextView = itemView.findViewById(R.id.textDescripcion)
        val textMonto: TextView = itemView.findViewById(R.id.textMonto)
        val textDetalle: TextView = itemView.findViewById(R.id.textDetalle)
    }

}