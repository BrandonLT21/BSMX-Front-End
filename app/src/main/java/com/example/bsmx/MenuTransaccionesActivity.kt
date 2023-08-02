package com.example.bsmx

import LineaDeCredito
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.journeyapps.barcodescanner.BarcodeEncoder

class MenuTransaccionesActivity : AppCompatActivity()  {
    private lateinit var btnGenerar: Button
    private lateinit var btnEscanear: Button
    private lateinit var btnCancelar: Button
    private lateinit var imvCodigo: ImageView
    private lateinit var txtCantidad: EditText
    private lateinit var txtCorreoT: EditText

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmación")
        builder.setMessage("¿Estás seguro de que deseas cancelar la realización de la transacción?")
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
        setContentView(R.layout.menu_transacciones)

        btnGenerar = findViewById(R.id.btnGenerar)
        btnEscanear = findViewById(R.id.btnEscanear)
        btnCancelar = findViewById(R.id.btnCancelar)
        imvCodigo = findViewById(R.id.imvCodigo)
        txtCantidad = findViewById(R.id.txtCantidad)
        txtCorreoT = findViewById(R.id.txtCorreoT)




        btnEscanear.setOnClickListener {

                initScanner()

        }

        btnGenerar.setOnClickListener{

            val cantidad = txtCantidad.text.toString()
            val correo = txtCorreoT.text.toString()
            val cantidad2 = txtCantidad.text.toString().toFloatOrNull()
            val lineaDeCredito = LineaDeCredito()

            if (cantidad2 != null && cantidad2 > lineaDeCredito.saldo) {
                Toast.makeText(this, "La cantidad ingresada es mayor a la de tu saldo actual", Toast.LENGTH_SHORT).show()
            }
            if(cantidad.isEmpty() || correo.isEmpty()){
                Toast.makeText(applicationContext, "Es obligatorio ingresar una cantidad y un email", Toast.LENGTH_SHORT).show()
            }else {

                try {
                    var barcodeEncoder: BarcodeEncoder = BarcodeEncoder()
                    var bitmap: Bitmap = barcodeEncoder.encodeBitmap(
                        txtCantidad.text.toString() + " solidarios. De: " + txtCorreoT.text.toString(),
                        BarcodeFormat.QR_CODE,
                        250,
                        250

                    )

                    imvCodigo.setImageBitmap(bitmap)


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }

        btnCancelar.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirmación")
            builder.setMessage("¿Estás seguro de que deseas cancelar la realización de la transacción?")
            builder.setPositiveButton("Sí") { dialog, which ->

                val intent = Intent(this, MenuBeneficiarioActivity::class.java)
                startActivity(intent)
            }
            builder.setNegativeButton("No") { dialog, which ->
                dialog.cancel()

            }
            val dialog = builder.create()
            dialog.show()

        }
    }

    private fun initScanner(){
        val cantidad = txtCantidad.text.toString().toFloatOrNull()
        val lineaDeCredito = LineaDeCredito()
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Escanea el codigo QR de la transacción")
        integrator.setBeepEnabled(true)
        integrator.initiateScan()



        if (cantidad != null) {
            lineaDeCredito.saldo = lineaDeCredito.saldo + cantidad
        }



        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result:IntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null){
            if (result.contents ==null){
                Toast.makeText(this, "Escaneo cancelado", Toast.LENGTH_SHORT).show()
            }else{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Transacción exitosa")
                builder.setMessage("El valor de la transacción es de: ${result.contents}")
                builder.setPositiveButton("Ok") { dialog, which ->
                    val intent = Intent(this, MenuBeneficiarioActivity::class.java)
                    startActivity(intent)

                }
                val dialog = builder.create()
                dialog.show()




            }
        }else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


}