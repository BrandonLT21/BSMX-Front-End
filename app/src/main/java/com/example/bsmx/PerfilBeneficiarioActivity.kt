package com.example.bsmx

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class PerfilBeneficiarioActivity : AppCompatActivity()  {

    private lateinit var btnCambiarFoto: Button
    private lateinit var imvFotoPerfil: ImageView
    private lateinit var btnRegresar: Button
    private val REQUEST_IMAGE_PICK = 3
    private var imageLoaded = false

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmación")
        builder.setMessage("¿Deseas regresar al menu principal?")
        builder.setPositiveButton("Sí") { dialog, which ->
            val intent = Intent(this, MenuBeneficiarioActivity::class.java)
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
        setContentView(R.layout.perfil_beneficiario)

        btnCambiarFoto = findViewById(R.id.btnCambiarFoto)
        imvFotoPerfil = findViewById(R.id.imvFotoPerfil)
        btnRegresar = findViewById(R.id.btnRegresar)


        btnCambiarFoto.setOnClickListener {
            openGallery()

        }

        btnRegresar.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirmación")
            builder.setMessage("¿Deseas regresar al menu principal?")
            builder.setPositiveButton("Sí") { dialog, which ->
                val intent = Intent(this, MenuBeneficiarioActivity::class.java)
                startActivity(intent)

            }

        }

    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_PICK -> {
                    val selectedImageUri: Uri? = data?.data
                    // Hacer algo con la imagen seleccionada desde la galería
                    imvFotoPerfil.setImageURI(selectedImageUri)
                    imageLoaded = true
                    Toast.makeText(this, "Imagen seleccionada desde la galería", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

}