package com.example.bsmx

import Transaccion
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class WalletHistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet_history)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewTransacciones)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val transacciones = mutableListOf<Transaccion>()
        // Agregar transacciones de ejemplo (puedes cargarlas desde una fuente de datos real)

        // Configurar el adaptador con la lista de transacciones
        val adapter = TransaccionAdapter(transacciones)
        recyclerView.adapter = adapter
    }
}