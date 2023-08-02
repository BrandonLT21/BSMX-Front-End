package com.example.bsmx

import EmpresaData
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class FormularioEmpresaActivity: AppCompatActivity() {


    private lateinit var txtNombre: EditText
    private lateinit var txtUtilidades: EditText
    private lateinit var txtPercepcionMensual: EditText
    private lateinit var txtCorreoElectronico: EditText
    private lateinit var txtGastos: EditText
    private lateinit var txtRfc: EditText
    private lateinit var txtAntiguedad: EditText
    private lateinit var txtGiro: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button
    private lateinit var rdbProducto: RadioButton
    private lateinit var rdbServicio: RadioButton
    private var empresaData: EmpresaData? = null

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmación")
        builder.setMessage("¿Estás seguro de que deseas cancelar el proceso de registro?")
        builder.setPositiveButton("Sí") { dialog, which ->
            val intent = Intent(this, MenuPrincipalActivity::class.java)
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
        setContentView(R.layout.formulario_empresa)

        txtNombre = findViewById(R.id.txtNombre)
        txtUtilidades = findViewById(R.id.txtUtilidades)
        txtPercepcionMensual = findViewById(R.id.txtPercepcionMensual)
        txtCorreoElectronico = findViewById(R.id.txtCorreo)
        txtGastos = findViewById(R.id.txtGastos)
        txtRfc = findViewById(R.id.txtRfc)
        txtAntiguedad = findViewById(R.id.txtAntiguedad)
        txtGiro = findViewById(R.id.txtGiro)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnCancelar = findViewById(R.id.btnCancelar)
        rdbProducto = findViewById(R.id.rdbProducto)
        rdbServicio = findViewById(R.id.rdbServicio)


        btnGuardar.setOnClickListener() {
            val nombre = txtNombre.text.toString()
            val utilidades = txtUtilidades.text.toString()
            val percepcionMensual = txtPercepcionMensual.text.toString()
            val correoElectronico = txtCorreoElectronico.text.toString()
            val gastos = txtGastos.text.toString()
            val rfc = txtRfc.text.toString()
            val antiguedad = txtAntiguedad.text.toString()
            val giro = txtGiro.text.toString()
            val producto = rdbProducto.text.toString()
            val servicio = rdbServicio.text.toString()

            if (nombre.isEmpty() || utilidades.isEmpty() || percepcionMensual.isEmpty() || correoElectronico.isEmpty() ||
                gastos.isEmpty() || antiguedad.isEmpty() || giro.isEmpty()
            ) {
                Toast.makeText(
                    applicationContext,
                    "Es obligatorio llenar todos los campos",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                empresaData = EmpresaData(
                    nombre,
                    utilidades,
                    percepcionMensual,
                    correoElectronico,
                    gastos,
                    rfc,
                    antiguedad,
                    giro,
                    producto,
                    servicio
                )

                val intent = Intent(this, FormularioUbicacionActivity::class.java)
                intent.putExtra("empresaData", empresaData)
                startActivity(intent)


            }


        }

        btnCancelar.setOnClickListener() {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirmación")
            builder.setMessage("¿Estás seguro de que deseas cancelar el proceso de registro?")
            builder.setPositiveButton("Sí") { dialog, which ->

                val intent = Intent(this, MenuPrincipalActivity::class.java)
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