package com.rodrigo.soares.lista.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.rodrigo.soares.lista.R
import com.rodrigo.soares.lista.models.Item
import kotlinx.android.synthetic.main.row_list_item_selected_list_page.view.*
import java.text.NumberFormat
import java.util.*

class ItemListAdapter(var mContext: Context, var items: List<Item>) : ArrayAdapter<Item>(mContext, R.layout.row_list_item_selected_list_page, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view : View = convertView ?: LayoutInflater.from(mContext).inflate(R.layout.row_list_item_selected_list_page, parent, false)
        view.tvNameItemRow.text = items[position].title
        view.tvPriceItemRow.text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(items[position].price)
        return view
    }
}