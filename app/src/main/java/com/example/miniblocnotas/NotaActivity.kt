package com.example.miniblocnotas

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray

class NotaActivity : AppCompatActivity() {

    private var posicion: Int = -1
    private lateinit var editTexto: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nota)

        editTexto = findViewById(R.id.editTextoNota)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        posicion = intent.getIntExtra("posicion", -1)
        val contenido = intent.getStringExtra("contenido")
        if (contenido != null) {
            editTexto.setText(contenido)
        }

        btnGuardar.setOnClickListener {
            val prefs = getSharedPreferences("Notas", Context.MODE_PRIVATE)
            val json = prefs.getString("lista", "[]")
            val jsonArray = JSONArray(json)

            if (posicion >= 0) {
                jsonArray.put(posicion, editTexto.text.toString())
            } else {
                jsonArray.put(editTexto.text.toString())
            }

            prefs.edit().putString("lista", jsonArray.toString()).apply()
            finish()
        }
    }
}
