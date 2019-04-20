package com.rodrigo.soares.lista.dao.impl

import android.content.ContentValues
import android.provider.BaseColumns

import com.rodrigo.soares.lista.dao.BasicDAO
import com.rodrigo.soares.lista.database.DBConnection
import com.rodrigo.soares.lista.models.Account

import java.util.ArrayList

class AccountDAO(private val connection: DBConnection): BasicDAO<Account> {
    override fun save(entity: Account): Boolean {
        val db = connection.writableDatabase
        val values = ContentValues()

        values.put("Income", entity.income)

        val newRowId = db.insert("Account", null, values)

        return newRowId != -1L
    }

    override fun getById(id: Int): Account {
        val db = connection.readableDatabase

        val projection = arrayOf(BaseColumns._ID, "Income")

        val selection = BaseColumns._ID + " = ?"
        val selectionArgs = arrayOf(Integer.toString(id))

        val cursor = db.query(
            "Account",
            projection,
            selection,
            selectionArgs, null, null, null
        )
        cursor.moveToNext()
        val accountId = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
        val accountIncome = cursor.getDouble(cursor.getColumnIndexOrThrow("Income"))
        cursor.close()
        return Account(accountId, accountIncome)
    }

    override fun getAll(): List<Account> {
        val db = connection.readableDatabase

        val projection = arrayOf(BaseColumns._ID, "Income")

        val accounts = ArrayList<Account>()

        val cursor = db.query(
            "Account",
            projection,
            null,
            null, null, null, null
        )

        while (cursor.moveToNext()) {
            val accountId = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
            val accountIncome = cursor.getDouble(cursor.getColumnIndexOrThrow("Income"))

            accounts.add(Account(accountId, accountIncome))
        }
        cursor.close()

        return accounts
    }

    override fun remove(id: Int): Int {
        val db = connection.writableDatabase

        val selection = "${BaseColumns._ID} LIKE ?"
        val selectionArgs = arrayOf(Integer.toString(id))

        return db.delete("Account", selection, selectionArgs)
    }

    override fun remove(entiry: Account): Int {
        return remove(entiry.id!!)
    }

    override fun update(entiry: Account): Int {
        val db = connection.writableDatabase

        val values = ContentValues()
        values.put("Income", entiry.income)

        val selection = BaseColumns._ID + " LIKE ?"
        val selectionArgs = arrayOf(Integer.toString(entiry.id!!))

        return db.update(
            "Account",
            values,
            selection,
            selectionArgs)
    }
}