package com.rodrigo.soares.lista.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.rodrigo.soares.lista.database.DBConnection
import com.rodrigo.soares.lista.R
import com.rodrigo.soares.lista.extensions.setNightMode
import com.rodrigo.soares.lista.models.Item
import com.rodrigo.soares.lista.models.Lista
import com.rodrigo.soares.lista.presenters.ListaSelecionadaPresenter
import kotlinx.android.synthetic.main.activity_lista_selecionada.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class ListaSelecionadaActivity : AppCompatActivity() {

    private var mPresenter: ListaSelecionadaPresenter? = null
    private var dbConnection: DBConnection? = null
    private var mAdapter: ArrayAdapter<Item>? = null
    private val itens: MutableList<Item> by lazy { mutableListOf<Item>() }
    private var lista: Lista? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setNightMode()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_selecionada)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        mPresenter = ListaSelecionadaPresenter(this)
        dbConnection = DBConnection(this)
        lista = intent.getSerializableExtra("listaSelecionada") as Lista
        itens.addAll(Item.getItemsByIdLista(dbConnection!!, lista?.id!!.toInt()))
        mAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, itens)

        supportActionBar?.title = lista?.titulo
        lvItens.adapter = mAdapter
        registerForContextMenu(lvItens)

        mPresenter?.toggleViewsVisibilityEvent()

        fabAddItem.setOnClickListener {
            mPresenter?.openKeyBoard()
        }

        etNomeItem.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val item = Item(etNomeItem.text.toString().trim(), "", lista?.id)
                addItemAttListaItens(item)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        btnAddItem.setOnClickListener {
            val item = Item(etNomeItem.text.toString().trim(), "", lista?.id)
            addItemAttListaItens(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_INSERT && resultCode == Activity.RESULT_OK){
            val listaEditada = data?.getSerializableExtra(EditarListaActivity.LISTA_EDITADA_EXTRA_STRING) as Lista
            mPresenter?.attListaInfo(listaEditada)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.lista_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.lista_toolbar_menu_editar ->{
                val intent = Intent(this, EditarListaActivity::class.java)
                intent.putExtra("lista", lista)
                startActivityForResult(intent, REQUEST_INSERT)
                return true
            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return false
    }

    //Remover depois de implementar o recycler view
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        val inflater = menuInflater
        inflater.inflate(R.menu.item_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.item_menu_descricao -> {
                mPresenter?.openKeyBoard()
                return true
            }
            R.id.item_menu_deletar -> {
                val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
                val itemId = itens[info.position].id
                Item.removerItemById(dbConnection!!, itemId!!)
                itens.removeAt(info.position)
                mAdapter?.notifyDataSetChanged()
                return true
            }
        }
        return false
    }

    fun getConnection(): DBConnection = dbConnection as DBConnection

    fun addItemAttListaItens(item: Item){
        mPresenter?.addItem(item)
        itens.clear()
        itens.addAll(Item.getItemsByIdLista(dbConnection!!, lista?.id!!.toInt()))
        mAdapter?.notifyDataSetChanged()
    }

    companion object {
        val REQUEST_INSERT = 0
    }

}
