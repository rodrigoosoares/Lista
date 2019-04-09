package com.rodrigo.soares.lista.dao

interface BasicDAO<T> {

    fun save(entity: T): Boolean

    fun getById(id: Int): T

    fun getAll(): List<T>

    fun remove(id: Int): Int

    fun remove(entiry: T): Int

    fun update(entiry: T): Int
}