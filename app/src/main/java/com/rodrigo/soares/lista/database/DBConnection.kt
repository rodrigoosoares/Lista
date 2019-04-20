package com.rodrigo.soares.lista.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.rodrigo.soares.lista.models.Account

import com.rodrigo.soares.lista.models.Item
import com.rodrigo.soares.lista.models.Lista

class DBConnection(context: Context?) : SQLiteOpenHelper(context, DATABSE_NAME, null, DATABSE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(Lista.CREATE_TABLE_LISTA_QUERY)
        db.execSQL(Item.CREATE_TABLE_ITEM_QUERY)
        db.execSQL(Account.CREATE_TABLE_ACCOUNT_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(Lista.DROP_TABLE_LISTA_QUERY)
        db.execSQL(Item.DROP_TABLE_ITEM_QUERY)
        db.execSQL(Account.DROP_TABLE_ACCOUNT_QUERY)
        onCreate(db)
    }

    companion object {
        val DATABSE_VERSION = 1
        val DATABSE_NAME = "Listas.db"
    }
}
