package com.rodrigo.soares.lista.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.rodrigo.soares.lista.R
import com.rodrigo.soares.lista.dao.impl.ItemDAO
import com.rodrigo.soares.lista.database.DBConnection
import com.rodrigo.soares.lista.extensions.setNightMode
import com.rodrigo.soares.lista.models.Item
import com.rodrigo.soares.lista.models.Lista
import com.rodrigo.soares.lista.presenters.SelectedListPresenter
import kotlinx.android.synthetic.main.activity_lista_selecionada.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class SelectedListActivity : AppCompatActivity() {

    private var mPresenter: SelectedListPresenter? = null
    private var mConnection: DBConnection? = null
    private var mAdapter: ArrayAdapter<Item>? = null

    private val itens: MutableList<Item> by lazy { mutableListOf<Item>() }
    private var list: Lista? = null
    private var itemDAO: ItemDAO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setNightMode()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_selecionada)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        mPresenter = SelectedListPresenter(this)
        mConnection = DBConnection(this)
        val itemDAO = ItemDAO(mConnection!!)

        list = intent.getSerializableExtra("listaSelecionada") as Lista
        itens.addAll(itemDAO.getAllByIdLista(list?.id!!))
        mAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, itens)

        supportActionBar?.title = list?.titulo
        lvItens.adapter = mAdapter
        registerForContextMenu(lvItens)

        mPresenter?.toggleViewsVisibilityEvent()

        fabAddItem.setOnClickListener {
            mPresenter?.openKeyBoard()
        }

        etNomeItem.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val item = Item(etNomeItem.text.toString().trim(), "", list?.id)
                addItemAttListaItens(item)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        btnAddItem.setOnClickListener {
            val item = Item(etNomeItem.text.toString().trim(), "", list?.id)
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
                intent.putExtra("lista", list)
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

    //Remove after implements the recycler view
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
                itemDAO!!.remove(itemId!!)
                itens.removeAt(info.position)
                mAdapter?.notifyDataSetChanged()
                return true
            }
        }
        return false
    }

    fun getItemDAO() = itemDAO

    fun addItemAttListaItens(item: Item){
        mPresenter?.addItem(item)
        itens.clear()
        itens.addAll(itemDAO!!.getAllByIdLista(list?.id!!))
        mAdapter?.notifyDataSetChanged()
    }

    companion object {
        val REQUEST_INSERT = 0
    }

}
