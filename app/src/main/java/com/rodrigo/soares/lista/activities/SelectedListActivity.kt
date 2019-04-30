package com.rodrigo.soares.lista.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
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

    override fun onDestroy() {
        super.onDestroy()
        mConnection!!.close()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_INSERT && resultCode == Activity.RESULT_OK){
            val editedList = data?.getSerializableExtra(EditListActivity.EDITED_LIST_EXTRA_STRING) as Lista
            mPresenter?.attListaInfo(editedList)
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

    private fun setUp(){
        setContentView(R.layout.activity_selected_list)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        window.statusBarColor = ResourcesCompat.getColor(resources, R.color.colorDark, null)

        mPresenter = SelectedListPresenter(this)
        mConnection = DBConnection(this)
        itemDao = ItemDAO(mConnection!!)
        selectedList = intent.getSerializableExtra(SELECTED_LIST_EXTRA) as Lista
        mAdapter = RecyclerViewItemsAdapter(this,items)

        supportActionBar?.title = selectedList?.titulo
        items.addAll(mPresenter!!.getAllItems(itemDao!!, selectedList!!))
        mPresenter!!.setUpDragNDropRecyclerView(mAdapter!!)
        mPresenter!!.setListNameTitle(selectedList!!)

        fabAddItem.setOnClickListener {
            mPresenter?.toAddItemPage()
        }
    }

    fun getItemDao() = itemDao!!

    fun getSelectedList() = selectedList!!

    fun getPresenter() = mPresenter!!

    fun attListItems(){
        items.clear()
        items.addAll(itemDao!!.getAllByIdLista(selectedList?.id!!).sortedBy { it.position })
        mAdapter?.notifyDataSetChanged()
    }

    companion object {
        val REQUEST_INSERT = 0
        val LIST_TO_EDIT = "editList"
    }

}
