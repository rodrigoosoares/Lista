package com.rodrigo.soares.lista.models

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import com.rodrigo.soares.lista.DataBase.DBConnection
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

    // Métodos da Serialização
    fun adicionarItem(item: Item) {
        this.itens?.add(item)
    }

    fun removerItem(item: Item) {
        this.itens?.remove(item)
    }

    companion object {

        val CREATE_TABLE_LISTA_QUERY = "CREATE TABLE Lista (" + BaseColumns._ID + " INTEGER PRIMARY KEY, Titulo TEXT, CorTitulo TEXT, Position INTEGER)"
        val DROP_TABLE_LISTA_QUERY = "DROP TABLE IF EXISTS Lista"

        // Métodos do Banco de Dados
        fun salvarLista(context: Context?, lista: Lista): Boolean {
            val connection = DBConnection(context)
            val db = connection.writableDatabase
            val values = ContentValues()

            values.put("Titulo", lista.titulo.trim())
            values.put("CorTitulo", lista.corTitulo)
            values.put("Position", this.getListas(connection).size)

            val newRowId = db.insert("Lista", null, values)
            connection.close()
            return newRowId != -1L

        }

        fun getListas(connection: DBConnection): ArrayList<Lista> {
            val db = connection.readableDatabase

            val projection = arrayOf(BaseColumns._ID, "Titulo", "CorTitulo", "Position")

            val sortOrder = "Position ASC"

            val cursor = db.query(
                    "Lista",
                    projection, null, null, null, null,
                    sortOrder
            )

            val listas = ArrayList<Lista>()
            while (cursor.moveToNext()) {
                val listaId = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
                val listaTitulo = cursor.getString(cursor.getColumnIndexOrThrow("Titulo"))
                val listaCorTItulo = cursor.getString(cursor.getColumnIndexOrThrow("CorTitulo"))
                val position = cursor.getInt(cursor.getColumnIndexOrThrow("Position"))
                listas.add(Lista(listaId, listaTitulo, listaCorTItulo, position))
            }
            cursor.close()

            return listas
        }

        fun getListaById(connection: DBConnection, id: Int): Lista {
            val db = connection.readableDatabase

            val projection = arrayOf(BaseColumns._ID, "Titulo", "CorTitulo", "Position")

            val selection = BaseColumns._ID + " = ?"
            val selectionArgs = arrayOf(Integer.toString(id))

            val cursor = db.query(
                    "Lista",
                    projection,
                    selection,
                    selectionArgs, null, null, null
            )
            cursor.moveToNext()
            val listaId = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
            val listaTitulo = cursor.getString(cursor.getColumnIndexOrThrow("Titulo"))
            val listaCorTItulo = cursor.getString(cursor.getColumnIndexOrThrow("CorTitulo"))
            val position = cursor.getInt(cursor.getColumnIndexOrThrow("Position"))
            cursor.close()
            return Lista(listaId, listaTitulo, listaCorTItulo, position)
        }

        fun removerListaById(connection: DBConnection, id: Int) {
            val db = connection.writableDatabase

            val selection = BaseColumns._ID + " LIKE ?"
            val selectionArgs = arrayOf(Integer.toString(id))
            db.delete("Lista", selection, selectionArgs)
        }

        fun editarLista(connection: DBConnection, listaNova: Lista) {
            val db = connection.writableDatabase

            val values = ContentValues()
            values.put("Titulo", listaNova.titulo.trim())
            values.put("CorTitulo", listaNova.corTitulo)
            values.put("Position", listaNova.position)

            val selection = BaseColumns._ID + " LIKE ?"
            val selectionArgs = arrayOf(Integer.toString(listaNova.id!!.toInt()))

            val count = db.update(
                    "Lista",
                    values,
                    selection,
                    selectionArgs)
        }
    }
}
