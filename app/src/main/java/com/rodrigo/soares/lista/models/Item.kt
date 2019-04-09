package com.rodrigo.soares.lista.models

import android.provider.BaseColumns
import java.io.Serializable

class Item (var titulo: String, var descricao: String, var idLista: Int?): Serializable, BaseColumns {

    var id: Int? = null

    constructor(id: Int, titulo: String, descricao: String, idLista: Int?): this(titulo, descricao, idLista){
        this.id = id
    }

    override fun toString(): String = titulo

    companion object {

        val CREATE_TABLE_ITEM_QUERY =
            "CREATE TABLE Item (" + BaseColumns._ID + " INTEGER PRIMARY KEY, Titulo TEXT, Descricao TEXT, IdLista INTEGER)"
        val DROP_TABLE_ITEM_QUERY = "DROP TABLE IF EXISTS Item"
    }
}
