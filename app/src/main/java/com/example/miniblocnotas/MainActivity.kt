package com.example.miniblocnotas

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotasAdapter
    private var listaNotas = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerViewNotas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = NotasAdapter(listaNotas,
            onEdit = { posicion ->
                val intent = Intent(this, NotaActivity::class.java)
                intent.putExtra("posicion", posicion)
                intent.putExtra("contenido", listaNotas[posicion])
                startActivity(intent)
            },
            onDelete = { posicion ->
                listaNotas.removeAt(posicion)
                guardarNotas()
                adapter.notifyItemRemoved(posicion)
            }
        )
        recyclerView.adapter = adapter

        val fab = findViewById<FloatingActionButton>(R.id.fabAgregar)
        fab.setOnClickListener {
            startActivity(Intent(this, NotaActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        cargarNotas()
    }

    private fun cargarNotas() {
        val prefs = getSharedPreferences("Notas", Context.MODE_PRIVATE)
        val json = prefs.getString("lista", "[]")
        val jsonArray = JSONArray(json)
        listaNotas.clear()
        for (i in 0 until jsonArray.length()) {
            listaNotas.add(jsonArray.getString(i))
        }
        adapter.notifyDataSetChanged()
    }

    private fun guardarNotas() {
        val prefs = getSharedPreferences("Notas", Context.MODE_PRIVATE).edit()
        val jsonArray = JSONArray()
        listaNotas.forEach { jsonArray.put(it) }
        prefs.putString("lista", jsonArray.toString())
        prefs.apply()
    }
}
