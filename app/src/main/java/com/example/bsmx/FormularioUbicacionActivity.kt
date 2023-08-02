package com.example.bsmx

import EmpresaData
import EmpresaUbicacionData
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class FormularioUbicacionActivity: AppCompatActivity() {
    private lateinit var txtEstado: EditText
    private lateinit var txtMunicipio: EditText
    private lateinit var txtLocalidad: EditText
    private lateinit var txtCalle: EditText
    private lateinit var txtColonia: EditText
    private lateinit var txtNumExterior: EditText
    private lateinit var txtNumInterior: EditText
    private lateinit var txtCodigoPostal: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button
    private var empresaUbicacionData: EmpresaUbicacionData? = null


    override fun onBackPressed() {


        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmación")
        builder.setMessage("¿Deseas regresar al registro de información de la empresa?")
        builder.setPositiveButton("Sí") { dialog, which ->
            val intent = Intent(this, FormularioEmpresaActivity::class.java)
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
        setContentView(R.layout.formulario_ubicacion)

        txtEstado = findViewById(R.id.txtEstado)
        txtMunicipio = findViewById(R.id.txtMunicipio)
        txtLocalidad = findViewById(R.id.txtLocalidad)
        txtCalle = findViewById(R.id.txtCalle)
        txtColonia = findViewById(R.id.txtColonia)
        txtNumExterior = findViewById(R.id.txtNumExterior)
        txtNumInterior = findViewById(R.id.txtNumInterior)
        txtCodigoPostal = findViewById(R.id.txtCodigoPostal)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnCancelar = findViewById(R.id.btnCancelar)

        btnGuardar.setOnClickListener() {

            val estado = txtEstado.text.toString()
            val municipio = txtMunicipio.text.toString()
            val localidad = txtLocalidad.text.toString()
            val calle = txtCalle.text.toString()
            val colonia = txtColonia.text.toString()
            val numExterior = txtNumExterior.text.toString()
            val numInterior = txtNumExterior.text.toString()
            val codigoPostal = txtCodigoPostal.text.toString()
            val empresaData = intent.getSerializableExtra("empresaData") as? EmpresaData

            if (estado.isEmpty() || municipio.isEmpty() || localidad.isEmpty() || calle.isEmpty() ||
                    colonia.isEmpty() || numExterior.isEmpty() || numInterior.isEmpty() || codigoPostal.isEmpty()
            ) {
            Toast.makeText(
                applicationContext,
                "Es obligatorio llenar todos los campos",
                Toast.LENGTH_SHORT
            ).show()

        } else {

            empresaUbicacionData = EmpresaUbicacionData(
                estado,
                municipio,
                localidad,
                calle,
                colonia,
                numExterior,
                numInterior,
                codigoPostal
            )

                val intent = Intent(this, FormularioRepresentanteActivity::class.java)
                intent.putExtra("empresaUbicacionData", empresaUbicacionData)
                intent.putExtra("empresaData", empresaData)
                startActivity(intent)


        }
        }

        btnCancelar.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirmación")
            builder.setMessage("¿Estás seguro de que deseas cancelar el proceso de registro?")
            builder.setPositiveButton("Sí") { dialog, which ->

                val intent = Intent(this, FormularioEmpresaActivity::class.java)
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