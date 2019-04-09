package com.rodrigo.soares.lista.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.rodrigo.soares.lista.database.DBConnection
import com.rodrigo.soares.lista.R
import com.rodrigo.soares.lista.adapters.SpinnerCoresAdapter
import com.rodrigo.soares.lista.models.Cores
import com.rodrigo.soares.lista.models.Lista
import com.rodrigo.soares.lista.presenters.EditarListaPresenter
import kotlinx.android.synthetic.main.activity_editar_lista.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class EditarListaActivity : AppCompatActivity() {

    private var mPresenter: EditarListaPresenter? = null
    private var dbConnection: DBConnection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_lista)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val lista = intent.getSerializableExtra("lista") as Lista
        mPresenter = EditarListaPresenter(this)
        dbConnection = DBConnection(this)
        val spinnerAdapter = SpinnerCoresAdapter(this)

        supportActionBar?.title = "Editar lista: ${lista.titulo}"
        etEditarNomeLista.setText(lista.titulo)
        spEditarCorLista.adapter = spinnerAdapter

        Cores.cores.forEachIndexed { index, element ->
            if(element.equals(lista.corTitulo)) {
                spEditarCorLista.setSelection(index)
            }
        }

        btnEditarSalvar.setOnClickListener {
            val listaEditava = Lista(lista.id!!.toInt(), etEditarNomeLista.text.toString(), spEditarCorLista.selectedItem.toString(), lista.position!!.toInt())
            val resultIntent = Intent()
            resultIntent.putExtra(LISTA_EDITADA_EXTRA_STRING, listaEditava)
            setResult(Activity.RESULT_OK, resultIntent)
            mPresenter?.salvarListaAlteracoes(listaEditava)
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun getConnection() = dbConnection

    companion object {
        val LISTA_EDITADA_EXTRA_STRING = "listaEditada"
    }
}
