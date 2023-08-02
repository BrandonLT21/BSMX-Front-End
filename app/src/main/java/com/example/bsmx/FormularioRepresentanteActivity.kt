package com.example.bsmx

import EmpresRepresentanteData
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
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class FormularioRepresentanteActivity: AppCompatActivity(), DomicilioCallback {

    private lateinit var txtNombre: EditText
    private lateinit var txtAPaterno: EditText
    private lateinit var txtAMaterno: EditText
    private lateinit var txtCurp: EditText
    private lateinit var txtFechaN: EditText
    private lateinit var txtTelefono: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button
    private lateinit var rdbActa: RadioButton
    private lateinit var rdbPoder: RadioButton
    private lateinit var rdbOtro: RadioButton
    private var idBeneficiario: Int = 0;
    private var idDomicilio: Int = 0;
    private var empresaRepresentanteData: EmpresRepresentanteData? = null

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmación")
        builder.setMessage("¿Deseas regresar al formulario de datos de ubicación de la empresa?")
        builder.setPositiveButton("Sí") { dialog, which ->
            val intent = Intent(this, FormularioUbicacionActivity::class.java)
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
        setContentView(R.layout.formulario_representante)

        txtNombre = findViewById(R.id.txtNombre)
        txtAPaterno = findViewById(R.id.txtAPaterno)
        txtAMaterno = findViewById(R.id.txtAMaterno)
        txtCurp = findViewById(R.id.txtCurp)
        txtFechaN = findViewById(R.id.txtFechaN)
        txtTelefono = findViewById(R.id.txtTelefono)
        rdbActa = findViewById(R.id.rdbActa)
        rdbPoder = findViewById(R.id.rdbPoder)
        rdbOtro = findViewById(R.id.rdbOtro)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnCancelar = findViewById(R.id.btnCancelar)
        val empresaData = intent.getSerializableExtra("empresaData") as? EmpresaData
        val empresaUbicacionData = intent.getSerializableExtra("empresaUbicacionData") as? EmpresaUbicacionData

        btnGuardar.setOnClickListener(){

            val nombre = txtNombre.text.toString()
            val aPaterno = txtAPaterno.text.toString()
            val aMaterno = txtAMaterno.text.toString()
            val curp = txtCurp.text.toString()
            val fechaN = txtFechaN.text.toString()
            val telefono = txtTelefono.text.toString()
            val acta = rdbActa.text.toString()
            val poder = rdbPoder.text.toString()
            val otro = rdbOtro.text.toString()

            if (nombre.isEmpty() || aPaterno.isEmpty() || aMaterno.isEmpty() || curp.isEmpty() ||
                fechaN.isEmpty() || telefono.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Es obligatorio llenar todos los campos",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                empresaUbicacionData?.let {
                    enviarDatosDomicilio(it, this)
                }



                val intent = Intent(this, MenuPrincipalActivity::class.java)
                startActivity(intent)

                Toast.makeText(
                    applicationContext,
                    "Empresa registrada con exito",
                    Toast.LENGTH_SHORT
                ).show()


            }

        }

        btnCancelar.setOnClickListener {
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

    override fun onDomicilioUpdated(id: Int) {

        }
    


    private fun enviarDatosDomicilio(empresaUbicacionData: EmpresaUbicacionData, callback: DomicilioCallback) {
        val url = "https://bsmx-api.onrender.com/domicilios"

        val requestBody = FormBody.Builder()
            .add("colonia", empresaUbicacionData.colonia)
            .add("codigoPostal", empresaUbicacionData.codigoPostal)
            .add("estado", empresaUbicacionData.estado)
            .add("numeroInterior", empresaUbicacionData.numInterior)
            .add("numeroExterior", empresaUbicacionData.numExterior)
            .add("municipio", empresaUbicacionData.municipio)
            .add("calle", empresaUbicacionData.calle)
            .add("localidad", empresaUbicacionData.localidad)
            .build()


        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                val jsonObject = JSONObject(responseBody)
                if (response.isSuccessful) {
                    runOnUiThread {
                        val id = jsonObject.getInt("id")
                        idDomicilio = id
                        callback.onDomicilioUpdated(id)
                    }
                } else {
                    runOnUiThread {
                        val message = jsonObject.getString("message")
                        println( "Error: $message")
                    }
                }
            }
        })
    }





}