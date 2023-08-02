package com.example.bsmx

import BeneficiarioData
import PersonaData
import PersonaDomicilioData
import PersonaNacimientoData
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

class FormularioNacimientoActivity : AppCompatActivity() {


    private lateinit var txtEstadoN: EditText
    private lateinit var txtFechaN: EditText
    private lateinit var txtLocalidadN: EditText
    private lateinit var txtMunicipioN: EditText
    private lateinit var btnRegistrarDatos: Button
    private lateinit var btnCancelar: Button
    private var personaNacimientoData: PersonaNacimientoData? = null

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmación")
        builder.setMessage("¿Deseas regresar al formulario de domicilio?")
        builder.setPositiveButton("Sí") { dialog, which ->
            val intent = Intent(this, FormularioDomicilioActivity::class.java)
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
        setContentView(R.layout.forrmulario_nacimiento)




        txtEstadoN = findViewById(R.id.txtEstadoN)
        txtMunicipioN = findViewById(R.id.txtMunicipioN)
        txtLocalidadN = findViewById(R.id.txtLocalidadN)
        txtFechaN = findViewById(R.id.txtFechaN)
        btnRegistrarDatos = findViewById(R.id.btnRegistrarDatos)
        btnCancelar = findViewById(R.id.btnCancelar)
        val personaData = intent.getSerializableExtra("personaData") as? PersonaData
        val beneficiarioData = intent.getSerializableExtra("beneficiarioData") as? BeneficiarioData
        val personaDomicilioData = intent.getSerializableExtra("personaDomicilioData") as? PersonaDomicilioData

        btnRegistrarDatos.setOnClickListener {

            val estado = txtEstadoN.text.toString()
            val municipio = txtMunicipioN.text.toString()
            val localidad = txtLocalidadN.text.toString()
            val fechaNacimiento = txtFechaN.text.toString()

            if(estado.isEmpty() || municipio.isEmpty() || localidad.isEmpty() || fechaNacimiento.isEmpty() ){
                Toast.makeText(applicationContext, "Es obligatorio llenar todos los campos", Toast.LENGTH_SHORT).show()

            }else {

                personaNacimientoData = PersonaNacimientoData(
                    estado,
                    municipio,
                    localidad,
                    fechaNacimiento

                )


                val intent = Intent(this, FormularioReferenciasActivity::class.java)
                intent.putExtra("personaData", personaData)
                intent.putExtra("beneficiarioData", beneficiarioData)
                intent.putExtra("personaDomicilioData", personaDomicilioData)
                intent.putExtra("personaNacimientoData", personaNacimientoData)
                startActivity(intent)
            }


            //guardarDatos(estado, municipio, localidad, fechaNacimiento)



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

        /*private fun guardarDatos(estado:String,municipio:String,localidad:String, fechaNacimiento:String  ){
            val client = OkHttpClient()

            val url = ""
            val requestBody = FormBody.Builder()

                .add("estado", estado)
                .add("municipio", municipio)
                .add("localidad", localidad)
                .add("fechaNacimiento", fechaNacimiento)
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
