package com.rodrigo.soares.lista.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.rodrigo.soares.lista.DataBase.DBConnection
import com.rodrigo.soares.lista.R
import com.rodrigo.soares.lista.adapters.ReclyclerViewListasAdapter
import com.rodrigo.soares.lista.helpers.DynamicEventHelper
import com.rodrigo.soares.lista.models.Item
import com.rodrigo.soares.lista.models.Lista
import com.rodrigo.soares.lista.presenters.PaginaPrincipalPresenter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class PaginaPrincipalActivity : AppCompatActivity() {

    private var mPresenter: PaginaPrincipalPresenter? = null
    private var dbConnection: DBConnection? = null
    private var mAdapter: ReclyclerViewListasAdapter? = null

    private val listas: MutableList<Lista> by lazy { mutableListOf<Lista>()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mPresenter = PaginaPrincipalPresenter(this)
        dbConnection = DBConnection(this)
        //this.deleteDatabase("Listas.db")
        listas.addAll(Lista.getListas(dbConnection!!))
        mAdapter = ReclyclerViewListasAdapter(this, listas)

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
            mPresenter?.openDialogAddLista()
        }
    }

    override fun onResume() {
        super.onResume()
        atualizarLista()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        val inflater = menuInflater
        inflater.inflate(R.menu.lista_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.lista_menu_deletar -> {
                val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
                Lista.removerListaById(dbConnection!!, listas[info.position].id!!.toInt())
                Item.removerItemByIdLista(dbConnection!!, listas[info.position].id!!.toInt())
                atualizarListaDelete()
                return true
            }
        }
        return false
    }

    fun atualizarLista(){
        listas.clear()
        listas.addAll(Lista.getListas(dbConnection!!))
        mAdapter?.notifyDataSetChanged()
    }
    fun atualizarListaDelete(){
        listas.clear()
        listas.addAll(Lista.getListas(dbConnection!!))
        mAdapter = ReclyclerViewListasAdapter(this, listas)
        rvListas.adapter = mAdapter
    }

    fun getConnection(): DBConnection{
            return dbConnection as DBConnection
    }

    fun closeConnection(){
        dbConnection?.close()
    }

    fun getPresenter() = mPresenter

}
