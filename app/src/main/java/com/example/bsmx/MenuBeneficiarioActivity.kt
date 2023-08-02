package com.example.bsmx

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MenuBeneficiarioActivity : AppCompatActivity() {

    private lateinit var btnPerfil: Button
    private lateinit var btnHistorial: Button
    private lateinit var btnTransacciones: Button
    private lateinit var btnCerrarSesion: Button

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmación")
        builder.setMessage("¿Estás seguro de que deseas cerrar la sesión actual?")
        builder.setPositiveButton("Sí") { dialog, which ->
            val intent = Intent(this, MainActivity::class.java)
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
        setContentView(R.layout.menu_beneficiario)

        btnPerfil = findViewById(R.id.btnPerfil)
        btnHistorial = findViewById(R.id.btnHistorial)
        btnTransacciones = findViewById(R.id.btnTransacciones)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)


        btnTransacciones.setOnClickListener{
            val intent = Intent(this, MenuTransaccionesActivity::class.java)
            startActivity(intent)
        }

        btnHistorial.setOnClickListener{
            val intent = Intent(this, HistorialBeneficiarioActivity::class.java)
            startActivity(intent)
        }

        btnPerfil.setOnClickListener{
            val intent = Intent(this, PerfilBeneficiarioActivity::class.java)
            startActivity(intent)

        }





        btnCerrarSesion.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirmación")
            builder.setMessage("¿Estás seguro de que deseas cerrar la sesión actual?")
            builder.setPositiveButton("Sí") { dialog, which ->
                Toast.makeText(applicationContext, "¡Hasta pronto!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            builder.setNegativeButton("No") { dialog, which ->
                dialog.cancel()
            }
            val dialog = builder.create()
            dialog.show()
        }


    }


}