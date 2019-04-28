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
import com.rodrigo.soares.lista.adapters.ReclyclerViewListsAdapter
import com.rodrigo.soares.lista.dao.impl.ListaDAO
import com.rodrigo.soares.lista.database.DBConnection
import com.rodrigo.soares.lista.extensions.setNightMode
import com.rodrigo.soares.lista.models.Lista
import com.rodrigo.soares.lista.presenters.MainPagePresenter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import com.rodrigo.soares.lista.R


class MainPageActivity : AppCompatActivity() {

    private var mPresenter: MainPagePresenter? = null
    private var mConnection: DBConnection? = null
    private var mAdapter: ReclyclerViewListsAdapter? = null

    private val lists: MutableList<Lista> by lazy { mutableListOf<Lista>()}
    private var listDao: ListaDAO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setNightMode()
        super.onCreate(savedInstanceState)
        //TODO tranferir esse cara para a SplashScreen
//            this.deleteDatabase("Listas.db")
//            mConnection = DBConnection(this)
        setUp()
    }

    override fun onResume() {
        super.onResume()
        updateLists()
    }

    override fun onDestroy() {
        super.onDestroy()
        mConnection?.close()
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
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        window.statusBarColor = ResourcesCompat.getColor(resources, R.color.colorDark, null)

        mPresenter = MainPagePresenter(this)
        mConnection = DBConnection(this)
        mAdapter = ReclyclerViewListsAdapter(this, lists)
        listDao = ListaDAO(mConnection!!)

        lists.addAll(mPresenter!!.getAllLists(listDao!!))
        mPresenter?.setUpDragNDropRecyclerView(mAdapter!!)
        fabAddList.setOnClickListener {
            mPresenter!!.openDialogAddList()
        }
    }

    fun updateLists(){
        lists.clear()
        lists.addAll(mPresenter!!.getAllLists(listDao!!).sortedBy { it.position })
        tvIncome.text = mPresenter!!.setUpIncomeText(lists)
        mAdapter?.notifyDataSetChanged()
    }

    fun getListDao() = listDao

    fun getConnection() = mConnection as DBConnection

    fun getPresenter() = mPresenter

    fun getRequestCode() = REQUEST_CONFIG

    companion object {
        val REQUEST_CONFIG = 0
    }
}
