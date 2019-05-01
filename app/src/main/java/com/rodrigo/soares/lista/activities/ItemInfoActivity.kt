package com.rodrigo.soares.lista.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.view.MenuItem
import com.rodrigo.soares.lista.R
import com.rodrigo.soares.lista.database.DBConnection
import com.rodrigo.soares.lista.models.Item
import com.rodrigo.soares.lista.presenters.ItemInfoPresenter
import kotlinx.android.synthetic.main.activity_item_info.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import java.text.NumberFormat
import java.util.*

class ItemInfoActivity : AppCompatActivity() {

    private val SELECTED_ITEM_EXTRA = "selectedItem"

    private var mPresenter: ItemInfoPresenter? = null
    private var mConnection: DBConnection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        mConnection!!.close()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        onBackPressed()
        return true
    }

    private fun setUp(){
        setContentView(R.layout.activity_item_info)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Item Info"
        window.statusBarColor = ResourcesCompat.getColor(resources, R.color.colorDark, null)

        val selectedItem = intent.getSerializableExtra(SELECTED_ITEM_EXTRA) as Item
        mConnection = DBConnection(this)
        mPresenter = ItemInfoPresenter(this)

        mPresenter!!.setUpItemInfo(selectedItem)
        btnDeleteItem.setOnClickListener {
            mPresenter!!.deleteItem(selectedItem)
        }
    }

    fun getConnection() = mConnection
}
