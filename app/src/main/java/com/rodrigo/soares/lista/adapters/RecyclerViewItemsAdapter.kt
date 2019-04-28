package com.rodrigo.soares.lista.adapters

import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rodrigo.soares.lista.R
import com.rodrigo.soares.lista.activities.SelectedListActivity
import com.rodrigo.soares.lista.models.Item
import kotlinx.android.synthetic.main.activity_selected_list.*
import kotlinx.android.synthetic.main.row_list_item_selected_list_page.view.*
import java.lang.IndexOutOfBoundsException
import java.text.NumberFormat
import java.util.*

class RecyclerViewItemsAdapter(val activity: SelectedListActivity, val items: MutableList<Item>) : RecyclerView.Adapter<RecyclerViewItemsAdapter.ViewHolder>() {

    private val REMOVE_TEXT_UNDO = "DESFAZER"
    private val REMOVE_LIST_SNACKBAR_TEXT = " excluído"

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_list_item_selected_list_page, parent, false)
        return ViewHolder(view, activity, items)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = items[position].title
        holder.price.text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(items[position].price)
    }

    fun onItemMove(initialPosition: Int, finalPosition: Int){
        if(initialPosition < items.size && finalPosition < items.size){
            if(initialPosition < finalPosition){
                for(i in initialPosition until finalPosition){
                    Collections.swap(items, i, i+1)
                }
            }else{
               for(i in initialPosition..finalPosition + 1){
                   Collections.swap(items, i , i-1)
               }
            }
        }
        Thread(Runnable {
            val itemMovedByUser = items[finalPosition]
            val itemMovedAuto = items[initialPosition]

            itemMovedByUser.position = finalPosition
            itemMovedAuto.position = initialPosition
            activity.getItemDao().update(itemMovedByUser)
            activity.getItemDao().update(itemMovedAuto)
        }).start()

        notifyItemMoved(initialPosition, finalPosition)
    }

    fun removeItem(position: Int){
        val removedItem = items.removeAt(position)
        notifyItemRemoved(position)
        Snackbar.make(activity.rlListaSelecionadaLayout, removedItem.title + REMOVE_LIST_SNACKBAR_TEXT, Snackbar.LENGTH_SHORT)
            .setAction(REMOVE_TEXT_UNDO){
                items.add(position, removedItem)
                notifyItemInserted(position)
            }
            .addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    if(event == DISMISS_EVENT_TIMEOUT || event == DISMISS_EVENT_CONSECUTIVE){
                        activity.getItemDao().remove(removedItem)
                    }
                }
            }).show()
    }

    class ViewHolder(view: View, activity: AppCompatActivity, items: MutableList<Item>) : RecyclerView.ViewHolder(view) {
        val name = view.tvNameItemRow
        val price = view.tvPriceItemRow
    }
}