package com.rodrigo.soares.lista.presenters

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import com.rodrigo.soares.lista.activities.AddItemActivity
import com.rodrigo.soares.lista.activities.SelectedListActivity
import com.rodrigo.soares.lista.adapters.RecyclerViewItemsAdapter
import com.rodrigo.soares.lista.dao.impl.ItemDAO
import com.rodrigo.soares.lista.helpers.DynamicEventHelper
import com.rodrigo.soares.lista.models.Lista
import kotlinx.android.synthetic.main.activity_selected_list.*

class SelectedListPresenter(val activity: SelectedListActivity) {

    private val SELECTED_LIST_EXTRA = "selectedList"

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

    fun attListaInfo(lista: Lista){
        activity.supportActionBar?.title = lista.titulo
    }
}


























//    fun openKeyBoard(){
//        inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        inputManager?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
//    }
//
//    fun toggleViewsVisibilityEvent(){
//        KeyboardVisibilityEvent.setEventListener(activity) {
//            if(activity.etNomeItem.visibility == View.VISIBLE && activity.btnAddItem.visibility == View.VISIBLE) {
//                activity.fabAddItem.show()
//                activity.etNomeItem.visibility = View.INVISIBLE
//                activity.btnAddItem.visibility = View.INVISIBLE
//            }
//            else if(activity.etNomeItem.visibility == View.INVISIBLE && activity.btnAddItem.visibility == View.INVISIBLE){
//                activity.fabAddItem.hide()
//                activity.etNomeItem.visibility = View.VISIBLE
//                activity.btnAddItem.visibility = View.VISIBLE
//                activity.etNomeItem.requestFocus()
//            }
//        }
//    }
