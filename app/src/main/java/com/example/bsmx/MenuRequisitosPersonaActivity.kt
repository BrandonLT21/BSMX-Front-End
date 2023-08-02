package com.example.bsmx

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MenuRequisitosPersonaActivity: AppCompatActivity() {

    private lateinit var spnDocumento: Spinner
    private lateinit var imageView: ImageView
    private lateinit var btnGuardar: Button
    private val REQUEST_CAMERA_PERMISSION = 1
    private val REQUEST_IMAGE_CAPTURE = 2
    private val REQUEST_IMAGE_PICK = 3
    private var imageLoaded = false
    val requisitos = arrayOf(
        "CURP",
        "Comprobante domicilio",
        "Comprobante ingresos",
        "Foto casa",
        "Foto cara INE",
        "INE Frontal",
        "INE Reverso",
        "Pago de solicitud",
        "RFC"
    )

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmación")
        builder.setMessage("¿Estás seguro de que deseas cancelar la subida de requisitos?")
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
        setContentView(R.layout.menu_requisitos_persona)


        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,requisitos)
        spnDocumento = findViewById(R.id.spnDocumento)
        btnGuardar = findViewById(R.id.btnGuardar)
        spnDocumento.adapter = arrayAdapter
        imageView = findViewById(R.id.imageViewFoto)
        spnDocumento.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(applicationContext, "Requisito seleccionado: "+requisitos[position], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        val btnTakePhoto: Button = findViewById(R.id.btnTomarFoto)
        btnTakePhoto.setOnClickListener {
            //takePhoto()
            checkCameraPermission()

        }

        val btnSelectImage: Button = findViewById(R.id.btnBuscarGaleria)
        btnSelectImage.setOnClickListener {
            openGallery()

        }

        btnGuardar.setOnClickListener(){
            if(imageLoaded === true){
                val intent = Intent(this, MenuPrincipalActivity::class.java)
                startActivity(intent)
                Toast.makeText(
                    applicationContext,
                    "Requisito subido con exito",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                Toast.makeText(
                    applicationContext,
                    "Tienes que seleccionar una imagen",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        } else {
            openCamera()
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    // Hacer algo con la imagen tomada desde la cámara
                    imageView.setImageBitmap(imageBitmap)
                    imageLoaded = true

                    Toast.makeText(this, "Imagen tomada desde la cámara", Toast.LENGTH_SHORT).show()
                }
                REQUEST_IMAGE_PICK -> {
                    val selectedImageUri: Uri? = data?.data
                    // Hacer algo con la imagen seleccionada desde la galería
                    imageView.setImageURI(selectedImageUri)
                    imageLoaded = true


                    Toast.makeText(this, "Imagen seleccionada desde la galería", Toast.LENGTH_SHORT).show()
                }
            }
        }




    }




}