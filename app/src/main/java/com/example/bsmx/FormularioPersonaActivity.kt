package com.example.bsmx

import BeneficiarioData
import PersonaData
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import java.io.Serializable
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import android.view.View
import androidx.appcompat.app.AlertDialog


class FormularioPersonaActivity : AppCompatActivity() {

    private lateinit var txtCorreo: EditText
    private lateinit var txtNombre: EditText
    private lateinit var txtMaterno: EditText
    private lateinit var txtPaterno: EditText
    private lateinit var txtCurp: EditText
    private lateinit var txtOcupacion: EditText
    private lateinit var txtTelefono: EditText
    private lateinit var txtPercepcion: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button
    private var personaData: PersonaData? = null
    private var beneficiarioData: BeneficiarioData? = null
    private var contrasenia: String = "hola123"
    private var foto: String = ""
    private var idDomicilio: Int = 0;




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
        setContentView(R.layout.formulario_persona)


        txtCorreo = findViewById(R.id.txtCorreo)
        txtNombre = findViewById(R.id.txtNombre)
        txtPaterno = findViewById(R.id.txtPaterno)
        txtMaterno = findViewById(R.id.txtMaterno)
        txtCurp = findViewById(R.id.txtCurp)
        txtOcupacion = findViewById(R.id.txtOcupacion)
        txtPercepcion = findViewById(R.id.txtPMensual)
        txtTelefono = findViewById(R.id.txtTelefono)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnCancelar = findViewById(R.id.btnCancelar)



        btnGuardar.setOnClickListener{
            val nombre = txtNombre.text.toString()
            val aPaterno = txtPaterno.text.toString()
            val aMaterno = txtMaterno.text.toString()
            val correo = txtCorreo.text.toString()
            val ocupacion = txtOcupacion.text.toString()
            val percepcion = txtPercepcion.text.toString()
            val telefono = txtTelefono.text.toString()
            val curp = txtCurp.text.toString()


            if(nombre.isEmpty() || aPaterno.isEmpty() || aMaterno.isEmpty() || correo.isEmpty() ||
                ocupacion.isEmpty() || percepcion.isEmpty() || telefono.isEmpty() || curp.isEmpty() ){
                Toast.makeText(applicationContext, "Es obligatorio llenar todos los campos", Toast.LENGTH_SHORT).show()

            }else {

                personaData = PersonaData(
                    nombre,
                    aPaterno,
                    aMaterno,
                    percepcion,
                    ocupacion,
                    correo,
                    telefono,
                    curp
                )

                beneficiarioData = BeneficiarioData(
                    nombre,
                    correo,
                    contrasenia,
                    foto,
                    idDomicilio
                )


                val intent = Intent(this, FormularioDomicilioActivity::class.java)
                intent.putExtra("personaData", personaData)
                intent.putExtra("beneficiarioData", beneficiarioData)
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

    /*private fun guardarDatos(nombre:String, aPaterno:String, aMaterno:String, correo:String,
                             ocupacion:String, percepcion:String, telefono:String, curp:String  ){
        val client = OkHttpClient()

        val url = ""
        val requestBody = FormBody.Builder()
            .add("nombre", nombre)
            .add("aPaterno", aPaterno)
            .add("aMaterno", aMaterno)
            .add("correo", correo)
            .add("ocupacion", ocupacion)
            .add("percepcion", percepcion)
            .add("telefono", telefono)
            .add("curp", curp)
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