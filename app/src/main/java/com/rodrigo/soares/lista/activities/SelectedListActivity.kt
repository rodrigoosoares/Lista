package com.rodrigo.soares.lista.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.rodrigo.soares.lista.R
import com.rodrigo.soares.lista.adapters.RecyclerViewItemsAdapter
import com.rodrigo.soares.lista.dao.impl.ItemDAO
import com.rodrigo.soares.lista.database.DBConnection
import com.rodrigo.soares.lista.extensions.setNightMode
import com.rodrigo.soares.lista.models.Item
import com.rodrigo.soares.lista.models.Lista
import com.rodrigo.soares.lista.presenters.SelectedListPresenter
import kotlinx.android.synthetic.main.activity_selected_list.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class SelectedListActivity : AppCompatActivity() {

    private val SELECTED_LIST_EXTRA = "listaSelecionada"

    private var mPresenter: SelectedListPresenter? = null
    private var mConnection: DBConnection? = null
    private var mAdapter: RecyclerViewItemsAdapter? = null

    private val items: MutableList<Item> by lazy { mutableListOf<Item>() }
    private var selectedList: Lista? = null
    private var itemDao: ItemDAO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setNightMode()
        super.onCreate(savedInstanceState)

        setUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_INSERT && resultCode == Activity.RESULT_OK){
            val listaEditada = data?.getSerializableExtra(EditListActivity.EDITED_LIST_EXTRA_STRING) as Lista
            mPresenter?.attListaInfo(listaEditada)
        }
    }

    override fun onResume() {
        super.onResume()
        attListItems()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.lista_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.list_toolbar_edit_menu ->{
                val intent = Intent(this, EditListActivity::class.java)
                intent.putExtra(LIST_TO_EDIT, selectedList)
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
                return true
            }
            R.id.item_menu_deletar -> {
                val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
                val itemId = items[info.position].id
                itemDao!!.remove(itemId!!)
                items.removeAt(info.position)
                mAdapter?.notifyDataSetChanged()
                return true
            }
        }
        return false
    }
    private fun setUp(){
        setContentView(R.layout.activity_selected_list)
        setSupportActionBar(toolbar)
        window.statusBarColor = ResourcesCompat.getColor(resources, R.color.colorDark, null)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        mPresenter = SelectedListPresenter(this)
        mConnection = DBConnection(this)
        itemDao = ItemDAO(mConnection!!)

        selectedList = intent.getSerializableExtra(SELECTED_LIST_EXTRA) as Lista
        items.addAll(mPresenter!!.getAllItems(itemDao!!, selectedList!!))
        mAdapter = RecyclerViewItemsAdapter(this,items)

        supportActionBar?.title = selectedList?.titulo
        rvItens.adapter = mAdapter
        //registerForContextMenu(rvItens)

        fabAddItem.setOnClickListener {
            mPresenter?.toAddItemPage()
        }
    }

    fun getItemDAO() = itemDao

    fun getSelectedList() = selectedList!!

    fun attListItems(){
        items.clear()
        items.addAll(itemDao!!.getAllByIdLista(selectedList?.id!!))
        mAdapter?.notifyDataSetChanged()
    }

    companion object {
        val REQUEST_INSERT = 0
        val LIST_TO_EDIT = "editList"
    }

}
