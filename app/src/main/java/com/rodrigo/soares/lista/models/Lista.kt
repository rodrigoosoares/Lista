package com.rodrigo.soares.lista.models

import android.provider.BaseColumns
import java.io.Serializable
import java.util.*

class Lista constructor(var titulo: String, var corTitulo: String) : Serializable, BaseColumns {

    var id: Int? = null
    var itens: ArrayList<Item>? = null
    var position: Int? = null

    constructor(id: Int, titulo: String, corTitulo: String, position: Int) : this(titulo, corTitulo) {
        this.id = id
        this.titulo = titulo
        this.corTitulo = corTitulo
        this.position = position
    }


    override fun toString(): String = titulo

    // TODO Facade para os Métodos da Serialização
    fun adicionarItem(item: Item) {
        this.itens?.add(item)
    }

    fun removerItem(item: Item) {
        this.itens?.remove(item)
    }

    companion object {
        val CREATE_TABLE_LISTA_QUERY =
            "CREATE TABLE Lista (" + BaseColumns._ID + " INTEGER PRIMARY KEY, Titulo TEXT, CorTitulo TEXT, Position INTEGER)"
        val DROP_TABLE_LISTA_QUERY = "DROP TABLE IF EXISTS Lista"
    }
}
