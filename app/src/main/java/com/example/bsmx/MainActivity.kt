package com.example.bsmx

import Usuario
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import android.view.View
import android.content.Intent
import android.text.InputType
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {


    private lateinit var txtCorreoElectronico: EditText
    private lateinit var txtPassword: EditText
    private lateinit var btnIniciarSesion: Button
    val usuario = Usuario();

    override fun onBackPressed() {
        // Salir de la aplicación al presionar el botón de retroceso
        finishAffinity()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtCorreoElectronico = findViewById(R.id.txtCorreoElectronico)
        txtPassword = findViewById(R.id.txtPassword)
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion)

        val showPasswordButton = findViewById<Button>(R.id.showPasswordButton)

        showPasswordButton.setOnClickListener {
            val isPasswordVisible = txtPassword.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD

            if (isPasswordVisible) {
                txtPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            } else {
                txtPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }

            txtPassword.setSelection(txtPassword.text.length) // Mantiene el cursor al final del texto
        }


        btnIniciarSesion.setOnClickListener {
            val correoElectronico = txtCorreoElectronico.text.toString()
            val password = txtPassword.text.toString()

            if(correoElectronico.isEmpty() || password.isEmpty()){
                Toast.makeText(applicationContext, "Se deben llenar todos los campos", Toast.LENGTH_SHORT).show()

            }else{
                login(correoElectronico, password)



            }

        /*else{
                Toast.makeText(applicationContext, "Credenciales no validas. Intentelo nuevamente", Toast.LENGTH_SHORT).show()

            }*/

        }
    }

    fun main() {
        val url = "https://bsmx-api.onrender.com/beneficiarios"


        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()


        val request = Request.Builder()
            .url(url)
            .build()


        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Error al realizar la solicitud: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    println("Error: ${response.code}")
                    return
                }


                val responseBody = response.body?.string()


                println("Respuesta:")
                println(responseBody)
            }
        })
    }

   private fun login(correoElectronico: String, password: String) {
        val client = OkHttpClient()

        val url = "https://bsmx-api.onrender.com/beneficiarios/login"   //+correoElectronico
        val requestBody = FormBody.Builder()
            .add("correo", correoElectronico)
            .add("contrasenia", password)
            .build()

       val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                val jsonObject = JSONObject(responseBody)



               // val success = jsonObject.getInt()

                // Obtener el resultado de la respuesta
                //val success = jsonObject.getBoolean("success")


                runOnUiThread {
                    if (response.isSuccessful) {
                        // Iniciar la siguiente actividad o mostrar un mensaje de éxito
                        Toast.makeText(applicationContext, "Login exitoso", Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext, MenuPrincipalActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Mostrar un mensaje de error
                        val message = jsonObject.getString("message")
                        Toast.makeText(applicationContext, "Error: $message", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    // Mostrar un mensaje de error en caso de falla de la solicitud
                    Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}








