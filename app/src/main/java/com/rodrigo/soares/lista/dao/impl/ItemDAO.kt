package com.rodrigo.soares.lista.dao.impl

import android.content.ContentValues
import android.provider.BaseColumns
import com.rodrigo.soares.lista.dao.BasicDAO
import com.rodrigo.soares.lista.database.DBConnection
import com.rodrigo.soares.lista.models.Item
import java.util.*

class ItemDAO(private val connection: DBConnection) : BasicDAO<Item> {

    override fun save(entity: Item): Boolean {
        val db = connection.writableDatabase
        val values = ContentValues()

        values.put("Titulo", entity.titulo.trim())
        values.put("Descricao", entity.descricao.trim())
        values.put("IdLista", entity.idLista)

        val newRowId = db.insert("Item", null, values)

        return newRowId != -1L
    }

    override fun getById(id: Int): Item {
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

    override fun getAll(): List<Item> {
        val db = connection.readableDatabase

        val projection = arrayOf(BaseColumns._ID, "Titulo", "Descricao", "IdLista")

        val itens = ArrayList<Item>()

        val cursor = db.query(
            "Item",
            projection,
            null,
            null, null, null, null
        )

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

    fun getAllByIdLista(id: Int): List<Item>{
        val db = connection.readableDatabase

        val projection = arrayOf(BaseColumns._ID, "Titulo", "Descricao", "IdLista")

        val selection = "IdLista = ?"
        val selectionArgs = arrayOf(Integer.toString(id))

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

    fun getQtByIdLista(id: Int): Int = getAllByIdLista(id).size

    override fun remove(id: Int): Int {
        val db = connection.writableDatabase

        val selection = "${BaseColumns._ID} LIKE ?"
        val selectionArgs = arrayOf(Integer.toString(id))

        return db.delete("Item", selection, selectionArgs)
    }

    override fun remove(entiry: Item): Int {
        return remove(entiry.id!!)
    }

    fun removeByIdLista(id: Int) {
        val db = connection.writableDatabase

        val selection = "IdLista LIKE ?"
        val selectionArgs = arrayOf(Integer.toString(id))

        db.delete("Item", selection, selectionArgs)
    }

    override fun update(entiry: Item): Int {
        val db = connection.writableDatabase

        val values = ContentValues()
        values.put("Titulo", entiry.titulo)
        values.put("Descricao", entiry.descricao)
        values.put("IdLista", entiry.idLista)

        val selection = BaseColumns._ID + " LIKE ?"
        val selectionArgs = arrayOf(Integer.toString(entiry.id!!))

        return db.update(
            "Item",
            values,
            selection,
            selectionArgs)
    }
}