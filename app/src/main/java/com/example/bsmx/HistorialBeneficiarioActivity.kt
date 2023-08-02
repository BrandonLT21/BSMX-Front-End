package com.example.bsmx

import LineaDeCredito
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class HistorialBeneficiarioActivity : AppCompatActivity() {

    private lateinit var txtSaldo: TextView
    private lateinit var lineaDeCredito: LineaDeCredito
    private lateinit var btnRegresar: Button

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmación")
        builder.setMessage("¿Deseas regresar al menu principal?")
        builder.setPositiveButton("Sí") { dialog, which ->
            val intent = Intent(this, MenuBeneficiarioActivity::class.java)
            startActivity(intent)

        }
        builder.setNegativeButton("No") { dialog, which ->
            // No hacer nada, mantener la actividad abierta
        }
        val dialog = builder.create()
        dialog.show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.historial_beneficiario)

        lineaDeCredito = LineaDeCredito()
        txtSaldo = findViewById(R.id.txtSaldo)
        btnRegresar = findViewById(R.id.btnRegresarMenu)



        txtSaldo.setText(lineaDeCredito.saldo.toString())

        btnRegresar.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirmación")
            builder.setMessage("¿Deseas regresar al menu principal?")
            builder.setPositiveButton("Sí") { dialog, which ->
                val intent = Intent(this, MenuBeneficiarioActivity::class.java)
                startActivity(intent)

            }

        }



    }



}