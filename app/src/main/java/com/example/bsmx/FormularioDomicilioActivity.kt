package com.example.bsmx

import BeneficiarioData
import PersonaData
import PersonaDomicilioData
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class FormularioDomicilioActivity : AppCompatActivity(){

    private lateinit var txtColonia: EditText
    private lateinit var txtCodigoPostal: EditText
    private lateinit var txtEstado: EditText
    private lateinit var txtNumInterior: EditText
    private lateinit var txtNumExterior: EditText
    private lateinit var txtMunicipio: EditText
    private lateinit var txtCalle: EditText
    private lateinit var txtLocalidad: EditText
    private lateinit var btnGuardarDatos: Button
    private lateinit var btnCancelar: Button
    private var personaDomicilioData: PersonaDomicilioData? = null
    private var idDomicilio: Int = 0;

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmación")
        builder.setMessage("¿Deseas regresar al formulario de información personal?")
        builder.setPositiveButton("Sí") { dialog, which ->
            val intent = Intent(this, FormularioPersonaActivity::class.java)
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
        setContentView(R.layout.formulario_domicilio)

        txtColonia = findViewById(R.id.txtColonia)
        txtCodigoPostal = findViewById(R.id.txtCodigoPostal)
        txtEstado = findViewById(R.id.txtEstado)
        txtNumInterior = findViewById(R.id.txtNumInterior)
        txtNumExterior = findViewById(R.id.txtNumExterior)
        txtMunicipio = findViewById(R.id.txtMunicipio)
        txtCalle = findViewById(R.id.txtCalle)
        txtLocalidad = findViewById(R.id.txtLocalidad)
        btnGuardarDatos = findViewById(R.id.btnGuardarDatos)
        btnCancelar = findViewById(R.id.btnCancelar)
        val personaData = intent.getSerializableExtra("personaData") as? PersonaData
        val beneficiarioData= intent.getSerializableExtra("beneficiarioData") as? BeneficiarioData



        btnGuardarDatos.setOnClickListener {
            val colonia = txtColonia.text.toString()
            val codigoPostal = txtCodigoPostal.text.toString()
            val estado = txtEstado.text.toString()
            val numInterior = txtNumInterior.text.toString()
            val numExterior = txtNumExterior.text.toString()
            val municipio = txtMunicipio.text.toString()
            val calle = txtCalle.text.toString()
            val localidad = txtLocalidad.text.toString()


            if (colonia.isEmpty() || codigoPostal.isEmpty() || estado.isEmpty() || numInterior.isEmpty() ||
                numExterior.isEmpty() || municipio.isEmpty() || calle.isEmpty() || localidad.isEmpty()
            ) {
                Toast.makeText(
                    applicationContext,
                    "Es obligatorio llenar todos los campos",
                    Toast.LENGTH_SHORT
                ).show()

            } else {


                //guardarDatos(colonia, codigoPostal, estado, numInterior, numExterior, municipio, calle, localidad)
                personaDomicilioData = PersonaDomicilioData(
                    colonia,
                    codigoPostal,
                    estado,
                    numInterior,
                    numExterior,
                    municipio,
                    calle,
                    localidad

                )


                val intent = Intent(this, FormularioNacimientoActivity::class.java)
                intent.putExtra("personaData", personaData)
                intent.putExtra("beneficiarioData", beneficiarioData)
                intent.putExtra("personaDomicilioData", personaDomicilioData)
                startActivity(intent)

            }


        }

        btnCancelar.setOnClickListener{
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

    private fun enviarDatosDomicilio(personaDomicilioData: PersonaDomicilioData) {
        val url = "https://bsmx-api.onrender.com/domicilios"

        val requestBody = FormBody.Builder()
            .add("colonia", personaDomicilioData.colonia)
            .add("codigoPostal", personaDomicilioData.codigoPostal)
            .add("estado", personaDomicilioData.estado)
            .add("numInterior", personaDomicilioData.numInterior)
            .add("numExterior", personaDomicilioData.numExterior)
            .add("municipio", personaDomicilioData.municipio)
            .add("calle", personaDomicilioData.calle)
            .add("localidad", personaDomicilioData.localidad)
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
                if (response.isSuccessful) {
                    runOnUiThread {
                        val responseBody = response.body?.string()
                        val jsonObject = JSONObject(responseBody)
                        val id = jsonObject.getInt("id")
                        idDomicilio = id;
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            "Error en el registro: ${response.code}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
    }

    /*private fun guardarDatos(colonia:String, codigoPostal:String, estado:String, numInterior:String,
                             numExterior:String, municipio:String, calle:String, localidad:String  ){
        val client = OkHttpClient()

        val url = ""
        val requestBody = FormBody.Builder()
            .add("colonia", colonia)
            .add("codigoPostal", codigoPostal)
            .add("estado", estado)
            .add("numInterior", numInterior)
            .add("numExterior", numExterior)
            .add("municipio", municipio)
            .add("calle", calle)
            .add("localidad", localidad)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                val jsonObject = JSONObject(responseBody)

                // Obtener el resultado de la respuesta
                val success = jsonObject.getBoolean("success")
                val message = jsonObject.getString("message")

                runOnUiThread {
                    if (success) {
                        // Registro exitoso
                        Toast.makeText(applicationContext, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    } else {
                        // Mostrar mensaje de error
                        Toast.makeText(applicationContext, "Error: $message", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    // Mostrar mensaje de error en caso de fallo de la solicitud
                    Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            }
        })




    }*/
}