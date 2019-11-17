package com.example.eleccionkt.RecyclerViewCandidato

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eleccionkt.R
import kotlinx.android.synthetic.main.prototipo_candidato.view.*

class AdapterCandidato(private var mListCandidato:List<candidato>,private val mContext: Context, private val clickListener:(candidato)-> Unit):RecyclerView.Adapter<AdapterCandidato.CandidatoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterCandidato.CandidatoViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        return CandidatoViewHolder(layoutInflater.inflate(R.layout.prototipo_candidato,parent,false))
    }

    override fun onBindViewHolder(holder: CandidatoViewHolder, position: Int) {}
    override fun getItemCount(): Int  = mListCandidato.size
    fun setTask(candidatos : List<candidato>){
        mListCandidato  = candidatos
        notifyDataSetChanged()
    }
    fun getTask(): List<candidato> = mListCandidato
    class CandidatoViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bind(candi:candidato,context: Context,clickListener: (candidato) -> Unit){
            itemView.tvNombre_alumno.text = candi.nombre_usuario.toString()
            itemView.tvDescripcion.text = candi.descripcion.toString()
            itemView.tvPropuesta.text = candi.propuesta.toString()
            itemView.setOnClickListener{ clickListener(candi)}
        }
    }
}