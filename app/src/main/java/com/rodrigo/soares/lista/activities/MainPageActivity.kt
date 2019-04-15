package com.rodrigo.soares.lista.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.rodrigo.soares.lista.R
import com.rodrigo.soares.lista.adapters.ReclyclerViewListasAdapter
import com.rodrigo.soares.lista.dao.impl.ListaDAO
import com.rodrigo.soares.lista.database.DBConnection
import com.rodrigo.soares.lista.extensions.setNightMode
import com.rodrigo.soares.lista.helpers.DynamicEventHelper
import com.rodrigo.soares.lista.models.Lista
import com.rodrigo.soares.lista.presenters.MainPagePresenter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class MainPageActivity : AppCompatActivity() {

    private var mPresenter: MainPagePresenter? = null
    private var mConnection: DBConnection? = null
    private var mAdapter: ReclyclerViewListasAdapter? = null

    private val lists: MutableList<Lista> by lazy { mutableListOf<Lista>()}
    private var listaDao: ListaDAO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setNightMode()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        setUp()

        //Drag n Drop do RV
        val callback = object: DynamicEventHelper.DynamicEventsCallback{
            override fun onItemMove(InitialPosition: Int, FinalPosition: Int) {
                mAdapter?.onItemMove(InitialPosition, FinalPosition)
            }

            override fun removeItem(position: Int) {
                mAdapter?.removeItem(position)
            }

        }
        val androidItemTouchHelper = ItemTouchHelper(DynamicEventHelper(callback))
        androidItemTouchHelper.attachToRecyclerView(rvListas)

        rvListas.layoutManager = LinearLayoutManager(this)
        rvListas.adapter = mAdapter

        fabAddLista.setOnClickListener {
            mPresenter?.openDialogAddList()
        }
    }

    override fun onResume() {
        super.onResume()
        updateLists()
    }

    override fun onDestroy() {
        mConnection?.close()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_CONFIG && resultCode == Activity.RESULT_OK)
            recreate()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.main_menu_config -> {
                mPresenter?.toConfigPage()
                return true
            }
        }
        return false
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        val inflater = menuInflater
        inflater.inflate(R.menu.lista_menu, menu)
    }

    private fun setUp(){
        mPresenter = MainPagePresenter(this)
        mConnection = DBConnection(this)
        mAdapter = ReclyclerViewListasAdapter(this, lists)

        listaDao = ListaDAO(mConnection!!)
        lists.addAll(listaDao!!.getAll())

    }

    fun updateLists(){
        lists.clear()
        lists.addAll(ListaDAO(mConnection!!).getAll())
        mAdapter?.notifyDataSetChanged()
    }

    fun getListaDao() = listaDao

    fun getConnection() = mConnection as DBConnection

    fun getPresenter() = mPresenter

    fun getRequestCode() = REQUEST_CONFIG

    companion object {
        val REQUEST_CONFIG = 0
    }
}

/*
TODO
    Lista
-Mandar os métodos d aactivity para presenter e organizar o código de ambas
-
    Item
-Alterar a forma de adicionar um item (NOVA ACTIVITY com request return)
-

 */
