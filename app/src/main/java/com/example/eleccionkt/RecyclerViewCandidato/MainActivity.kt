package com.example.eleccionkt.RecyclerViewCandidato

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.eleccionkt.ActivityLogin
import com.example.eleccionkt.BaseDatos.BaseDatos
import com.example.eleccionkt.BaseDatos.VolleySingleton
import com.example.eleccionkt.BaseDatos.address
import com.example.eleccionkt.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var scontrol: String
    private lateinit var snip: String
    private lateinit var  viewAdapter: AdapterCandidato
    private lateinit var  viewManager: RecyclerView.LayoutManager
    val candidatoList: List<candidato> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val actividad = intent
        if (actividad != null && actividad.hasExtra("ncontrol") && actividad.hasExtra("nip")) {
            scontrol = actividad.getStringExtra("ncontrol")
            snip = actividad.getStringExtra("nip")
        } else {
            val admin = BaseDatos(this)
            val result = admin.Consulta("Select ncontrol,nip From usuario")
            if (result!!.moveToFirst()) {
                scontrol = result.getString(0)
                snip = result.getString(1)
                result.close()
                admin.close()
            } else {
                getUsuarios()
                startActivity(Intent(this, ActivityLogin::class.java))
            }
        }
        //Toast.makeText(this,"LOGIN: $scontrol" ,Toast.LENGTH_LONG).show()
        //RecyclerView
        viewManager = LinearLayoutManager(this)
        viewAdapter = AdapterCandidato(candidatoList,this,{ candi:candidato -> onItemClickListener(candi)})
        rvCandidato.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(DividerItemDecoration(this@MainActivity,DividerItemDecoration.VERTICAL))
        }
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or  ItemTouchHelper.RIGHT){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean { return false }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val candi = viewAdapter.getTask()
                val admin = BaseDatos(baseContext)
                if(admin.Ejecuta("DELETE FROM candidato WHERE id_candidato ="+
                    candi[position].id_candidato) == true) retrieveCandidato()
            }
        }).attachToRecyclerView(rvCandidato)
    }
    private fun onItemClickListener(candi:candidato){
        Toast.makeText(this,"click",Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        retrieveCandidato()
    }
    private fun retrieveCandidato(){
        val candidatoX = getCandidatos()
        viewAdapter.setTask(candidatoX!!)
    }
    fun getCandidatos():MutableList<candidato>{
        var candidato:MutableList<candidato> = ArrayList()
        val admin = BaseDatos(this)
        val query = admin.Consulta("Select * From candidato ORDER BY id_candidato")
        while (query!!.moveToNext()){
            val id = query.getInt(0)
            val nombre = query.getString(1)
            val descripcion = query.getString(2)
            val propuesta = query.getString(3)
            candidato.add(candidato(id,nombre,descripcion,propuesta))
        }
        query.close()
        admin.close()
        return candidato
    } // RecyclerView


    fun getUsuarios() { //funcion que carga la informacion de MySQL a SQLite
        val wsURL = address.IP + "WService/MostrarAlumnos.php"
        val admin = BaseDatos(this)
        admin.Ejecuta("DELETE FROM usuario")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, wsURL, null, Response.Listener { response ->
                val succ = response["success"]
                val msg = response["message"]
                val usuariosJson = response.getJSONArray("usuario")//name usuario (webservice)
                for (i in 0 until usuariosJson.length()) {
                    val ncontrol = usuariosJson.getJSONObject(i).getString("ncontrol")
                    val nombre = usuariosJson.getJSONObject(i).getString("nombre_alumno")
                    val sexo = usuariosJson.getJSONObject(i).getString("sexo")
                    val id_carrera = usuariosJson.getJSONObject(i).getString("id_carrera")
                    val nip = usuariosJson.getJSONObject(i).getString("nip")
                    val sentencia =
                        "INSERT INTO usuario(ncontrol,nombre_alumno,sexo,id_carrera,nip) Values('$ncontrol','$nombre','$sexo',$id_carrera,'$nip')"
                    var result = admin.Ejecuta(sentencia)
                    Toast.makeText(this, "Información cargada: " + result, Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error capa8: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
}
