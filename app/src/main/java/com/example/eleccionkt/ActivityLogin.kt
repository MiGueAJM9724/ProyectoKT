package com.example.eleccionkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.eleccionkt.BaseDatos.BaseDatos
import kotlinx.android.synthetic.main.activity_login.*

class ActivityLogin : AppCompatActivity() {
    private lateinit var scontrol: String
    private lateinit var  snip: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.hide()
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
                if (result!!.moveToFirst()){
                    scontrol = result.getString(0)
                    snip = result.getString(1)
                    result.close()
                    admin.close()
                }
            }
        }
    }
}
