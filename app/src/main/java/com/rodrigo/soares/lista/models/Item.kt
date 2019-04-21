package com.rodrigo.soares.lista.models

import android.provider.BaseColumns
import java.io.Serializable

class Item (internal var title: String, var price: Double, var idLista: Int?): Serializable, BaseColumns {

    var id: Int? = null

    constructor(id: Int, title: String, price: Double, idLista: Int?): this(title, price, idLista){
        this.id = id
    }

    override fun toString(): String = title

    companion object {

        val CREATE_TABLE_ITEM_QUERY =
            "CREATE TABLE Item (" + BaseColumns._ID + " INTEGER PRIMARY KEY, Titulo TEXT, Preco REAL, IdLista INTEGER)"
        val DROP_TABLE_ITEM_QUERY = "DROP TABLE IF EXISTS Item"
    }
}
