package com.example.eleccionkt.RecyclerViewCandidato

class candidato(id_candidato:Int, nombre_usuario: String,descripcion :String,propuesta:String) {
    var id_candidato :Int = 0
    var nombre_usuario:String = ""
    var descripcion:String = ""
    var propuesta:String = ""

    init {
        this.id_candidato = id_candidato
        this.nombre_usuario = nombre_usuario
        this.descripcion = descripcion
        this.propuesta = propuesta
    }
}