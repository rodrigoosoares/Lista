package com.rodrigo.soares.lista.dao.impl

import android.content.ContentValues
import android.provider.BaseColumns
import com.rodrigo.soares.lista.dao.BasicDAO
import com.rodrigo.soares.lista.database.DBConnection
import com.rodrigo.soares.lista.models.Lista
import java.util.*

class ListaDAO(private val connection: DBConnection) : BasicDAO<Lista> {

    override fun save(entity: Lista): Boolean {
        val db = connection.writableDatabase
        val values = ContentValues()

        values.put("Titulo", entity.titulo.trim())
        values.put("Position", this.getAll().size)

        val newRowId = db.insert("Lista", null, values)

        return newRowId != -1L
    }


    override fun getById(id: Int): Lista {
        val db = connection.readableDatabase

        val projection = arrayOf(BaseColumns._ID, "Titulo", "Position")

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
        val position = cursor.getInt(cursor.getColumnIndexOrThrow("Position"))
        cursor.close()
        return Lista(listaId, listaTitulo, position)
    }

    override fun getAll(): List<Lista> {
        val db = connection.readableDatabase

        val projection = arrayOf(BaseColumns._ID, "Titulo", "Position")

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
            val position = cursor.getInt(cursor.getColumnIndexOrThrow("Position"))
            listas.add(Lista(listaId, listaTitulo, position))
        }
        cursor.close()

        return listas
    }

    override fun remove(id: Int): Int {
        val db = connection.writableDatabase

        val selection = BaseColumns._ID + " LIKE ?"
        val selectionArgs = arrayOf(Integer.toString(id))
        return db.delete("Lista", selection, selectionArgs)

    }

    override fun remove(entiry: Lista): Int {
        return remove(entiry.id!!)
    }

    override fun update(entiry: Lista): Int {
        val db = connection.writableDatabase

        val values = ContentValues()
        values.put("Titulo", entiry.titulo.trim())
        values.put("Position", entiry.position)

        val selection = BaseColumns._ID + " LIKE ?"
        val selectionArgs = arrayOf(Integer.toString(entiry.id!!.toInt()))

        return db.update(
            "Lista",
            values,
            selection,
            selectionArgs)
    }
}