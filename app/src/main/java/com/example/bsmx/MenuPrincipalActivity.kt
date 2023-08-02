package com.example.bsmx

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MenuPrincipalActivity : AppCompatActivity() {



    private lateinit var rbPersona: RadioButton
    private lateinit var rbEmpresa: RadioButton
    private lateinit var btnRegistrar: Button
    private lateinit var btnCerrarSesion: Button
    private lateinit var btnRequisitos: Button


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
        setContentView(R.layout.menu_principal)


        rbPersona = findViewById(R.id.rbPersona)
        rbEmpresa = findViewById(R.id.rbEmpresa)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
        btnRequisitos = findViewById(R.id.btnRequisitos)

        btnRequisitos.setOnClickListener(){

            if (rbPersona.isChecked){
                val intent = Intent(this, MenuRequisitosPersonaActivity::class.java)
                startActivity(intent)
            }
            if(rbEmpresa.isChecked){
                val intent = Intent(this, MenuRequisitosActivity::class.java)
                startActivity(intent)

            }else if(rbEmpresa.isChecked == false && rbPersona.isChecked == false){
                // Mostrar un mensaje de error
                Toast.makeText(applicationContext, "Selecciona una opcion", Toast.LENGTH_SHORT).show()
            }
        }

        btnRegistrar.setOnClickListener {
             if (rbPersona.isChecked){
                 val intent = Intent(this, FormularioPersonaActivity::class.java)
                  startActivity(intent)

             }
            if(rbEmpresa.isChecked){
                val intent = Intent(this, FormularioEmpresaActivity::class.java)
                startActivity(intent)

            }else if(rbEmpresa.isChecked == false && rbPersona.isChecked == false){
                // Mostrar un mensaje de error
                Toast.makeText(applicationContext, "Selecciona una opcion", Toast.LENGTH_SHORT).show()
            }

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