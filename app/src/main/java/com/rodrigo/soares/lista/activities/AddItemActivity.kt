package com.rodrigo.soares.lista.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.rodrigo.soares.lista.R
import com.rodrigo.soares.lista.database.DBConnection
import com.rodrigo.soares.lista.models.Lista
import com.rodrigo.soares.lista.presenters.AddItemPresenter
import kotlinx.android.synthetic.main.activity_add_item.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class AddItemActivity : AppCompatActivity() {

    private val SELECTED_LIST_EXTRA = "selectedList"

    private var mPresenter: AddItemPresenter? = null
    private var mConnection: DBConnection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUp()
    }

    fun setUp(){
        setContentView(R.layout.activity_add_item)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        mPresenter = AddItemPresenter(this)
        mConnection = DBConnection(this)
        val selectedList = intent.getSerializableExtra(SELECTED_LIST_EXTRA) as Lista

        btnAddItem.setOnClickListener {
            mPresenter!!.saveItem(mConnection!!, selectedList)
        }

    }
}
