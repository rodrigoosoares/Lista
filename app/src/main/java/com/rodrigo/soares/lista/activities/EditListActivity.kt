package com.rodrigo.soares.lista.activities

import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import com.rodrigo.soares.lista.R
import com.rodrigo.soares.lista.adapters.SpinnerColorsAdapter
import com.rodrigo.soares.lista.dao.impl.ListaDAO
import com.rodrigo.soares.lista.database.DBConnection
import com.rodrigo.soares.lista.models.Colors
import com.rodrigo.soares.lista.models.Lista
import com.rodrigo.soares.lista.presenters.EditListPresenter
import kotlinx.android.synthetic.main.activity_editar_lista.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class EditListActivity : AppCompatActivity() {

    private val EDIT_LIST_ACTIONBAR_TITLE = "Editar lista: "

    private var mPresenter: EditListPresenter? = null
    private var mConnection: DBConnection? = null

    var listDao: ListaDAO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUp()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setUp(){
        setContentView(R.layout.activity_editar_lista)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        window.statusBarColor = ResourcesCompat.getColor(resources, R.color.colorDark, null)

        val list = intent.getSerializableExtra(SelectedListActivity.LIST_TO_EDIT) as Lista
        mPresenter = EditListPresenter(this)
        mConnection = DBConnection(this)
        listDao = ListaDAO(mConnection!!)
        val spinnerAdapter = SpinnerColorsAdapter(this)

        supportActionBar?.title = EDIT_LIST_ACTIONBAR_TITLE + list.titulo
        etEditarNomeLista.setText(list.titulo)
        spEditListColor.adapter = spinnerAdapter

        btnSaveEditedList.setOnClickListener {
            mPresenter!!.btnSavedEditedListOnClickListener(Lista(list.id!!.toInt(), etEditarNomeLista.text.toString(), list.position!!.toInt()))
        }
    }

    fun getListaDao() = listDao

    companion object {
        val EDITED_LIST_EXTRA_STRING = "editedList"
    }
}
