package com.rodrigo.soares.lista.presenters

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import com.rodrigo.soares.lista.activities.AddItemActivity
import com.rodrigo.soares.lista.activities.ItemInfoActivity
import com.rodrigo.soares.lista.activities.SelectedListActivity
import com.rodrigo.soares.lista.adapters.RecyclerViewItemsAdapter
import com.rodrigo.soares.lista.dao.impl.ItemDAO
import com.rodrigo.soares.lista.helpers.DynamicEventHelper
import com.rodrigo.soares.lista.models.Item
import com.rodrigo.soares.lista.models.Lista
import kotlinx.android.synthetic.main.activity_selected_list.*

class SelectedListPresenter(val activity: SelectedListActivity) {

    private val SELECTED_LIST_EXTRA = "selectedList"
    private val SELECTED_ITEM_EXTRA = "selectedItem"

    fun setUpDragNDropRecyclerView(rvItemsAdapter: RecyclerViewItemsAdapter) {
        val rvItems = activity.rvItems
        val callback = object : DynamicEventHelper.DynamicEventsCallback{
            override fun onItemMove(InitialPosition: Int, FinalPosition: Int) {
                rvItemsAdapter.onItemMove(InitialPosition, FinalPosition)
            }
            override fun removeItem(position: Int) {
                rvItemsAdapter.removeItem(position)
            }
        }
        val androidItemTouchHelper = ItemTouchHelper(DynamicEventHelper(callback))

        androidItemTouchHelper.attachToRecyclerView(rvItems)
        rvItems.layoutManager = LinearLayoutManager(activity)
        rvItems.adapter = rvItemsAdapter
    }

    fun toAddItemPage(){
        val intent = Intent(activity, AddItemActivity::class.java)
        intent.putExtra(SELECTED_LIST_EXTRA, activity.getSelectedList())
        activity.startActivity(intent)
    }

    fun getAllItems(itemDao: ItemDAO, selectedList: Lista) = itemDao.getAllByIdLista(selectedList.id!!)

    fun attListaInfo(list: Lista){
        activity.supportActionBar?.title = list.titulo
        activity.tvSelectedListTitle.text = list.titulo
    }

    fun setListNameTitle(selectedList: Lista) {
        activity.tvSelectedListTitle.text = selectedList.titulo
    }

    fun toItemInfoPage(selectedItem: Item) {
        val intent = Intent(activity, ItemInfoActivity::class.java)
        intent.putExtra(SELECTED_ITEM_EXTRA, selectedItem)
        activity.startActivity(intent)
    }
}
