package com.example.bsmx

import BeneficiarioData
import PersonaData
import PersonaDomicilioData
import PersonaNacimientoData
import PersonaReferenciasData
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit
import java.util.concurrent.CountDownLatch


class FormularioReferenciasActivity : AppCompatActivity(){

    private lateinit var txtNombre: EditText
    private lateinit var txtTelefono: EditText
    private lateinit var txtNombre2: EditText
    private lateinit var txtTelefono2: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button
    private lateinit var personaReferenciasData: PersonaReferenciasData
    private var idBeneficiario: Int? = null
    private  var idDomicilio: Int? = null
    private var numRequestsCompleted = 0

    val URL: String = "https://api.example.com/"




    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmación")
        builder.setMessage("¿Deseas regresar al formulario de datos de nacimiento?")
        builder.setPositiveButton("Sí") { dialog, which ->
            val intent = Intent(this, FormularioNacimientoActivity::class.java)
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
        setContentView(R.layout.formulario_referencias)


        txtNombre = findViewById(R.id.txtNombre)
        txtTelefono = findViewById(R.id.txtTelefono)
        txtNombre2 = findViewById(R.id.txtNombre2)
        txtTelefono2 = findViewById(R.id.txtTelefono2)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnCancelar = findViewById(R.id.btnCancelar)
        val personaData = intent.getSerializableExtra("personaData") as PersonaData
        val beneficiarioData = intent.getSerializableExtra("beneficiarioData") as BeneficiarioData
        val personaDomicilioData = intent.getSerializableExtra("personaDomicilioData") as PersonaDomicilioData
        val personaNacimientoData = intent.getSerializableExtra("personaNacimientoData") as PersonaNacimientoData



        btnGuardar.setOnClickListener {
            val nombre = txtNombre.text.toString()
            val telefono = txtTelefono.text.toString()
            val nombre2 = txtNombre2.text.toString()
            val telefono2 = txtTelefono2.text.toString()


            if (nombre.isEmpty() || telefono.isEmpty() || nombre2.isEmpty() || telefono2.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Es obligatorio dar los datos de al menos dos referencias",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                enviarDatosDomicilio(personaDomicilioData, object : DataCallback {
                    override fun onDataReceived(id: Int) {
                        idDomicilio = id
                        eviarRegistroBeneficiario(beneficiarioData, object : DataCallback {
                            override fun onDataReceived(id: Int) {
                                idBeneficiario = id
                                enviarDatosPersona(personaData, personaNacimientoData, object : DataCallback {
                                    override fun onDataReceived(id: Int) {
                                        // Todas las solicitudes se han completado, puedes continuar con la siguiente etapa de registro.
                                        val intent = Intent(this@FormularioReferenciasActivity, MenuPrincipalActivity::class.java)
                                        startActivity(intent)
                                        Toast.makeText(
                                            applicationContext,
                                            "Persona registrada con éxito",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    override fun onError(message: String) {
                                        println("Error al enviar datos de persona: $message")
                                    }
                                })
                            }

                            override fun onError(message: String) {
                                println("Error al enviar datos de beneficiario: $message")
                            }
                        })
                    }

                    override fun onError(message: String) {
                        println("Error al enviar datos de domicilio: $message")
                    }
                })
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


    private interface DataCallback {
        fun onDataReceived(id: Int)
        fun onError(message: String)
    }




    private fun enviarDatosPersona(personaData: PersonaData, personaNacimientoData: PersonaNacimientoData, callback: DataCallback) {
        val url = "https://bsmx-api.onrender.com/personas"

        val requestBody = FormBody.Builder()
            .add("apellidoPaterno", personaData.aPaterno)
            .add("apellidoMaterno", personaData.aMaterno)
            .add("percepcionMensual", personaData.percepcionMensual)
            .add("ocupacion", personaData.ocupacion)
            .add("telefono", personaData.telefono)
            .add("curp", personaData.curp)
            .add("estado", personaNacimientoData.estado)
            .add("municipio", personaNacimientoData.municipio)
            .add("localidad", personaNacimientoData.localidad)
            .add("fechaNacimiento", personaNacimientoData.fechaNacimiento)
            .add("beneficiario", idBeneficiario.toString())
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .callTimeout(30, TimeUnit.SECONDS)
            .build()


        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                val jsonObject = JSONObject(responseBody)
                if (response.isSuccessful) {
                    runOnUiThread {
                            println("Registro Exitoso")

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

    private fun eviarRegistroBeneficiario(beneficiarioData: BeneficiarioData, callback: DataCallback){
        val url = "https://bsmx-api.onrender.com/beneficiarios"

        val requestBody = FormBody.Builder()
            .add("nombre", beneficiarioData.nombre )
            .add("correo", beneficiarioData.correo)
            .add("contrasenia", beneficiarioData.contrasenia)
            .add("domicilio", beneficiarioData.domicilio.toString())
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .callTimeout(30, TimeUnit.SECONDS)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                val jsonObject = JSONObject(responseBody)
                if (response.isSuccessful) {
                    runOnUiThread {
                        val responseBody = response.body?.string()
                        val jsonObject = JSONObject(responseBody)
                        val id = jsonObject.getInt("id")
                        idBeneficiario = id;
                        callback.onDataReceived(id)




                    }
                } else {
                    runOnUiThread {
                        val message = jsonObject.getString("message")
                        println( "Error: $message")
                        callback.onError(message)

                    }
                }
            }
        })
    }

    private fun enviarDatosDomicilio(personaDomicilioData: PersonaDomicilioData, callback: DataCallback) {
        val url = "https://bsmx-api.onrender.com/domicilios"

        val requestBody = FormBody.Builder()
            .add("colonia", personaDomicilioData.colonia)
            .add("codigoPostal", personaDomicilioData.codigoPostal)
            .add("estado", personaDomicilioData.estado)
            .add("numeroInterior", personaDomicilioData.numInterior)
            .add("numeroExterior", personaDomicilioData.numExterior)
            .add("municipio", personaDomicilioData.municipio)
            .add("calle", personaDomicilioData.calle)
            .add("localidad", personaDomicilioData.localidad)
            .build()


        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .callTimeout(30, TimeUnit.SECONDS)
            .build()



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
                        callback.onDataReceived(id)





                    }
                } else {
                    runOnUiThread {
                        val message = jsonObject.getString("message")
                        println( "Error: $message")
                        callback.onError(message)

                    }
                }
            }
        })
    }



    private fun enviarDatosReferencias(nombre: String, telefono: String) {
        val url = "https://api.example.com/registrar"

        val requestBody = FormBody.Builder()
            .add("nombre", personaReferenciasData.nombre)
            .add("telefono", personaReferenciasData.telefono)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .callTimeout(30, TimeUnit.SECONDS)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    runOnUiThread {
                        println("Registro Exitoso")

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

    }



