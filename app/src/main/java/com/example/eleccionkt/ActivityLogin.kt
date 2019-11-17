package com.example.eleccionkt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.eleccionkt.BaseDatos.BaseDatos
import com.example.eleccionkt.RecyclerViewCandidato.MainActivity
import kotlinx.android.synthetic.main.activity_login.*

class ActivityLogin : AppCompatActivity() {
    private lateinit var scontrol: String
    private lateinit var  snip: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.hide() //desabilita el actionbar
        etncontrol.requestFocus()
        btnLogin.setOnClickListener {
            if(etncontrol.text!!.isEmpty() || etnip.text!!.isEmpty()){
                Toast.makeText(this,"No debe dejar campos vacios",Toast.LENGTH_SHORT).show()
                etncontrol.requestFocus()
            }else{
                val ncontrol = etncontrol.text.toString()
                val nip = etnip.text.toString()
                val admin = BaseDatos(this)
                val result = admin.Consulta("SELECT ncontrol,nip FROM usuario")
                if (result != null){
                    scontrol = result.getString(0)
                    snip = result.getString(1)
                    result.close()
                    admin.close()
                    if (ncontrol == scontrol && nip == snip) {
                        val actividad = Intent(this,
                            MainActivity::class.java)
                        actividad.putExtra("ncontrol",ncontrol)
                        actividad.putExtra("nip",nip)
                        Toast.makeText(this,"LOGIN: $ncontrol" ,Toast.LENGTH_LONG).show()
                        startActivity(actividad)
                    }
                    else Toast.makeText(this,"Su número de control o nip son incorrectos",Toast.LENGTH_SHORT).show()
                }else Toast.makeText(this,"Capa8",Toast.LENGTH_SHORT).show()
            }
        }
    }
}
