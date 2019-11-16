package com.example.eleccionkt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.eleccionkt.BaseDatos.BaseDatos
import com.example.eleccionkt.BaseDatos.VolleySingleton
import com.example.eleccionkt.BaseDatos.address
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var scontrol: String
    private lateinit var snip: String
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
        Toast.makeText(this,"LOGIN: $scontrol" ,Toast.LENGTH_LONG).show()
    }
    fun getUsuarios() {
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
                    Toast.makeText(this, "Información cargada: " + result, Toast.LENGTH_SHORT)
                        .show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error capa8: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }


}
