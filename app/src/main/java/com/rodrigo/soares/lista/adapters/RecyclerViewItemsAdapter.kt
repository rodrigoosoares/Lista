package com.rodrigo.soares.lista.adapters

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rodrigo.soares.lista.R
import com.rodrigo.soares.lista.models.Item
import kotlinx.android.synthetic.main.row_list_item_selected_list_page.view.*
import java.text.NumberFormat
import java.util.*

class RecyclerViewItemsAdapter(val activity: AppCompatActivity, val items: MutableList<Item>) : RecyclerView.Adapter<RecyclerViewItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_list_item_selected_list_page, parent, false)
        return ViewHolder(view, activity, items)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = items[position].title
        holder.price.text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(items[position].price)
    }



    class ViewHolder(view: View, activity: AppCompatActivity, items: MutableList<Item>) : RecyclerView.ViewHolder(view) {
        val name = view.tvNameItemRow
        val price = view.tvPriceItemRow
    }
}