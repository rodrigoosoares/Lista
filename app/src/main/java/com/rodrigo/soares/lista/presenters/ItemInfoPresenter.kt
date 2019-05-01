package com.rodrigo.soares.lista.presenters

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.View
import com.rodrigo.soares.lista.R
import com.rodrigo.soares.lista.activities.ItemInfoActivity
import com.rodrigo.soares.lista.dao.impl.ItemDAO
import com.rodrigo.soares.lista.models.Item
import kotlinx.android.synthetic.main.activity_item_info.*
import java.text.NumberFormat
import java.util.*

class ItemInfoPresenter(val activity: ItemInfoActivity) {

    fun setUpItemInfo(selectedItem: Item) {
        activity.tvItemInfoName.text = selectedItem.title
        activity.tvItemInfoPrice.text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(selectedItem.price)
        if(selectedItem.link == "")
            activity.tvItemInfoLinkTitle.visibility = View.INVISIBLE

        activity.tvItemInfoLink.text = selectedItem.link

    }

    fun deleteItem(selectedItem: Item) {
        val dialogBuild = AlertDialog.Builder(activity)

        dialogBuild.setMessage(R.string.dialogDeleteItem)
                   .setPositiveButton("Sim") { dialog, which ->
                       ItemDAO(activity.getConnection()!!).remove(selectedItem)
                       activity.finish()
                   }
                   .setNegativeButton("Cancelar") { dialog, which ->
                       dialog.cancel()
                   }
                   .create()
        dialogBuild.show()
    }

}