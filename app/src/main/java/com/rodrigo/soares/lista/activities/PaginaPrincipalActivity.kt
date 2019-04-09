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
import com.rodrigo.soares.lista.database.DBConnection
import com.rodrigo.soares.lista.R
import com.rodrigo.soares.lista.adapters.ReclyclerViewListasAdapter
import com.rodrigo.soares.lista.extensions.setNightMode
import com.rodrigo.soares.lista.helpers.DynamicEventHelper
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
        setNightMode()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mPresenter = PaginaPrincipalPresenter(this)
        dbConnection = DBConnection(this)
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

    override fun onDestroy() {
        dbConnection?.close()
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
                mPresenter?.toConfiguracoesPage()
                return true
            }
        }
        return false
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        val inflater = menuInflater
        inflater.inflate(R.menu.lista_menu, menu)
    }

    fun atualizarLista(){
        listas.clear()
        listas.addAll(Lista.getListas(dbConnection!!))
        mAdapter?.notifyDataSetChanged()
    }

    fun getConnection(): DBConnection{
            return dbConnection as DBConnection
    }

    fun getPresenter() = mPresenter

    fun getRequestCode() = REQUEST_CONFIG

    companion object {
        val REQUEST_CONFIG = 0
    }
}
