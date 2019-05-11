package com.rodrigo.soares.lista.presenters

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.LinearLayout
import com.rodrigo.soares.lista.activities.MainPageActivity
import com.rodrigo.soares.lista.activities.SelectedListActivity
import com.rodrigo.soares.lista.adapters.ReclyclerViewListsAdapter
import com.rodrigo.soares.lista.dao.impl.ItemDAO
import com.rodrigo.soares.lista.dao.impl.ListaDAO
import com.rodrigo.soares.lista.fragments.AddListDialogFragment
import com.rodrigo.soares.lista.helpers.DynamicEventHelper
import com.rodrigo.soares.lista.models.Item
import com.rodrigo.soares.lista.models.Lista
import kotlinx.android.synthetic.main.activity_main.*
import java.text.NumberFormat
import java.util.*

class MainPagePresenter(var activity: MainPageActivity) {
    private val FRAGMENT_TAG = "fragmentAdicionarLista"
    private val SELECTED_LIST_EXTRA = "listaSelecionada"

    fun setUpDragNDropRecyclerView(rvListsAdapter: ReclyclerViewListsAdapter){
        val rvLists = activity.rvLists
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
        rvLists.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        rvLists.adapter = rvListsAdapter
    }

    fun openDialogAddList(){
        val addListDialog = AddListDialogFragment()
        addListDialog.show(activity.supportFragmentManager, FRAGMENT_TAG)
    }

    fun toSelectedListActivity(selectedList: Lista){
        val intent = Intent(activity, SelectedListActivity::class.java)
        intent.putExtra(SELECTED_LIST_EXTRA, selectedList)
        activity.startActivity(intent)
    }

    fun getAllLists(listDao: ListaDAO) = listDao.getAll()

    fun setUpIncomeText(lists: List<Lista>): String {
        //val account = AccountDAO(activity.getConnection()).getById(DEFAULT_ACCOUNT_ID)
        val locale = Locale("pt", "BR")
        var totalSpending = 0.0
        val itens = ArrayList<Item>()

        lists.forEach {
            itens.addAll(ItemDAO(activity.getConnection()).getAllByIdLista(it.id!!))
        }

        itens.forEach { totalSpending += it.price }

        return NumberFormat.getCurrencyInstance(locale).format(totalSpending)
    }
}