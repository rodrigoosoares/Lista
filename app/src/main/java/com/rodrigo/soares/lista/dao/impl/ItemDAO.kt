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

        values.put("Titulo", entity.title.trim())
        values.put("Preco", entity.price)
        values.put("IdLista", entity.idLista)
        values.put("Link", entity.link)
        values.put("Position", entity.position)

        val newRowId = db.insert("Item", null, values)

        return newRowId != -1L
    }

    override fun getById(id: Int): Item {
        val db = connection.readableDatabase

        val projection = arrayOf(BaseColumns._ID, "Titulo", "Preco", "IdLista", "Link", "Position")

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
        val itemPreco = cursor.getDouble(cursor.getColumnIndexOrThrow("Preco"))
        val itemIdLista = cursor.getInt(cursor.getColumnIndexOrThrow("IdLista"))
        val itemlink = cursor.getString(cursor.getColumnIndexOrThrow("Link"))
        val itemPosition = cursor.getInt(cursor.getColumnIndexOrThrow("Position"))

        cursor.close()
        return Item(itemId, itemTitulo, itemPreco, itemIdLista, itemlink, itemPosition)
    }

    override fun getAll(): List<Item> {
        val db = connection.readableDatabase

        val projection = arrayOf(BaseColumns._ID, "Titulo", "Preco", "IdLista", "Link", "Position")

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
            val itemPreco = cursor.getDouble(cursor.getColumnIndexOrThrow("Preco"))
            val itemIdLista = cursor.getInt(cursor.getColumnIndexOrThrow("IdLista"))
            val itemLink = cursor.getString(cursor.getColumnIndexOrThrow("Link"))
            val itemPosition = cursor.getInt(cursor.getColumnIndexOrThrow("Position"))

            itens.add(Item(itemId, itemTitulo, itemPreco, itemIdLista, itemLink, itemPosition))
        }
        cursor.close()

        return itens
    }

    fun getAllByIdLista(id: Int): List<Item>{
        val db = connection.readableDatabase

        val projection = arrayOf(BaseColumns._ID, "Titulo", "Preco", "IdLista", "Link", "Position")

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
            val itemPreco = cursor.getDouble(cursor.getColumnIndexOrThrow("Preco"))
            val itemIdLista = cursor.getInt(cursor.getColumnIndexOrThrow("IdLista"))
            val itemLink = cursor.getString(cursor.getColumnIndexOrThrow("Link"))
            val itemPosition = cursor.getInt(cursor.getColumnIndexOrThrow("Position"))
            itens.add(Item(itemId, itemTitulo, itemPreco, itemIdLista, itemLink, itemPosition))
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
        values.put("Titulo", entiry.title)
        values.put("Preco", entiry.price)
        values.put("IdLista", entiry.idLista)
        values.put("Link", entiry.link)
        values.put("Position", entiry.position)

        val selection = BaseColumns._ID + " LIKE ?"
        val selectionArgs = arrayOf(Integer.toString(entiry.id!!))

        return db.update(
            "Item",
            values,
            selection,
            selectionArgs)
    }
}