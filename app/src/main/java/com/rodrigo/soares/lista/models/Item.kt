package com.rodrigo.soares.lista.models

import android.content.ContentValues
import android.provider.BaseColumns
import com.rodrigo.soares.lista.DataBase.DBConnection
import java.io.Serializable
import java.util.*

class Item (var titulo: String, var descricao: String, var idLista: Int?): Serializable, BaseColumns {

    var id: Int? = null

    constructor(id: Int, titulo: String, descricao: String, idLista: Int?): this(titulo, descricao, idLista){
        this.id = id
    }
    companion object {

        val CREATE_TABLE_ITEM_QUERY = "CREATE TABLE Item (" + BaseColumns._ID + " INTEGER PRIMARY KEY, Titulo TEXT, Descricao TEXT, IdLista INTEGER)"
        val DROP_TABLE_ITEM_QUERY = "DROP TABLE IF EXISTS Item"

        fun adicionarItem(connection: DBConnection, item: Item): Boolean {
            val db = connection.writableDatabase
            val values = ContentValues()

            values.put("Titulo", item.titulo.trim())
            values.put("Descricao", item.descricao.trim())
            values.put("IdLista", item.idLista)

            val newRowId = db.insert("Item", null, values)

            return newRowId != -1L
        }

        fun getItemById(connection: DBConnection, id: Int): Item {
            val db = connection.readableDatabase

            val projection = arrayOf(BaseColumns._ID, "Titulo", "Descricao", "IdLista")

            val selection = BaseColumns._ID + " = ?"
            val selectionArgs = arrayOf(Integer.toString(id))

            val cursor = db.query(
                    "Item",
                    projection,
                    selection,
                    selectionArgs, null, null, null
            )
            cursor.moveToNext()
            val itemId = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
            val itemTitulo = cursor.getString(cursor.getColumnIndexOrThrow("Titulo"))
            val itemDescricao = cursor.getString(cursor.getColumnIndexOrThrow("Descricao"))
            val itemIdLista = cursor.getInt(cursor.getColumnIndexOrThrow("IdLista"))
            cursor.close()
            return Item(itemId, itemTitulo, itemDescricao, itemIdLista)
        }

        fun getItemsByIdLista(connection: DBConnection, idLista: Int): ArrayList<Item> {
            val db = connection.readableDatabase

            val projection = arrayOf(BaseColumns._ID, "Titulo", "Descricao", "IdLista")

            val selection = "IdLista = ?"
            val selectionArgs = arrayOf(Integer.toString(idLista))

            val cursor = db.query(
                    "Item",
                    projection,
                    selection,
                    selectionArgs, null, null, null
            )
            val itens = ArrayList<Item>()
            while (cursor.moveToNext()) {
                val itemId = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
                val itemTitulo = cursor.getString(cursor.getColumnIndexOrThrow("Titulo"))
                val itemDescricao = cursor.getString(cursor.getColumnIndexOrThrow("Descricao"))
                val itemIdLista = cursor.getInt(cursor.getColumnIndexOrThrow("IdLista"))
                itens.add(Item(itemId, itemTitulo, itemDescricao, itemIdLista))
            }
            cursor.close()
            return itens
        }

        fun getItemsQtByIdLista(connection: DBConnection, idLista: Int): Int = getItemsByIdLista(connection, idLista).size

        fun removerItemById(connection: DBConnection, id: Int) {
            val db = connection.writableDatabase

            val selection = "${BaseColumns._ID} LIKE ?"
            val selectionArgs = arrayOf(Integer.toString(id))

            db.delete("Item", selection, selectionArgs)
        }

        fun removerItemByIdLista(connection: DBConnection, idLista: Int) {
            val db = connection.writableDatabase

            val selection = "IdLista LIKE ?"
            val selectionArgs = arrayOf(Integer.toString(idLista))

            db.delete("Item", selection, selectionArgs)
        }

        fun editarItem(connection: DBConnection, id: Int, itemNovo: Item) {
            val db = connection.writableDatabase

            val values = ContentValues()
            values.put("Titulo", itemNovo.titulo)
            values.put("Descricao", itemNovo.descricao)
            values.put("IdLista", itemNovo.idLista)

            val selection = BaseColumns._ID + " LIKE ?"
            val selectionArgs = arrayOf(Integer.toString(id))

            val count = db.update(
                    "Items",
                    values,
                    selection,
                    selectionArgs)
        }
    }

    override fun toString(): String = titulo
}
