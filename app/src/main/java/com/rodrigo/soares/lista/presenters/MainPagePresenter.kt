package com.rodrigo.soares.lista.presenters

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import com.rodrigo.soares.lista.activities.ConfiguracoesActivity
import com.rodrigo.soares.lista.activities.MainPageActivity
import com.rodrigo.soares.lista.activities.SelectedListActivity
import com.rodrigo.soares.lista.adapters.ReclyclerViewListsAdapter
import com.rodrigo.soares.lista.dao.impl.AccountDAO
import com.rodrigo.soares.lista.dao.impl.ListaDAO
import com.rodrigo.soares.lista.fragments.AddListDialogFragment
import com.rodrigo.soares.lista.helpers.DynamicEventHelper
import com.rodrigo.soares.lista.models.Lista
import kotlinx.android.synthetic.main.activity_main.*

class MainPagePresenter(var activity: MainPageActivity) {

    private val DEFAULT_ACCOUNT_ID = 1

    private val INCOME_TEXT = "Renda: "
    private val FRAGMENT_TAG = "fragmentAdicionarLista"
    private val SELECTED_LIST_EXTRA = "listaSelecionada"

    fun setUpDragNDropRecyclerView(rvListsAdapter: ReclyclerViewListsAdapter){
        val rvLists = activity.rvLists
        val fabAddList = activity.fabAddList
        val callback = object: DynamicEventHelper.DynamicEventsCallback{
            override fun onItemMove(InitialPosition: Int, FinalPosition: Int) {
                rvListsAdapter.onItemMove(InitialPosition, FinalPosition)
            }
            override fun removeItem(position: Int) {
                rvListsAdapter.removeItem(position)
            }
        }
        val androidItemTouchHelper = ItemTouchHelper(DynamicEventHelper(callback))

        androidItemTouchHelper.attachToRecyclerView(rvLists)

        rvLists.layoutManager = LinearLayoutManager(activity)
        rvLists.adapter = rvListsAdapter

        fabAddList.setOnClickListener {
            openDialogAddList()
        }
    }

    private fun openDialogAddList(){
        val addListDialog = AddListDialogFragment()
        addListDialog.show(activity.supportFragmentManager, FRAGMENT_TAG)
    }

    fun toSelectedListActivity(selectedList: Lista){
        val intent = Intent(activity, SelectedListActivity::class.java)
        intent.putExtra(SELECTED_LIST_EXTRA, selectedList)
        activity.startActivity(intent)
    }

    fun toConfigPage() {
        activity.startActivityForResult(Intent(activity, ConfiguracoesActivity::class.java), activity.getRequestCode())
    }

    fun getAllLists(listDao: ListaDAO) = listDao.getAll()

    fun setUpIncomeText(): String{
        var account = AccountDAO(activity.getConnection()).getById(DEFAULT_ACCOUNT_ID)
        return INCOME_TEXT + account.income.toString()

    }
}