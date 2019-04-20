package com.rodrigo.soares.lista.models

import android.provider.BaseColumns

class Account(var income: Double): BaseColumns {

    var id: Int? = null

    constructor(id: Int, income: Double): this(income){
        this.id = id
        this.income = income
    }

    override fun toString(): String {
        return this.income.toString()
    }

    companion object{
        val CREATE_TABLE_ACCOUNT_QUERY =
            "CREATE TABLE Account (" + BaseColumns._ID + " INTEGER PRIMARY KEY, Income REAL)"
        val DROP_TABLE_ACCOUNT_QUERY = "DROP TABLE IF EXISTS Account"
    }

}

