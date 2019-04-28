package com.rodrigo.soares.lista.presenters

import com.rodrigo.soares.lista.activities.AddItemActivity
import com.rodrigo.soares.lista.dao.impl.ItemDAO
import com.rodrigo.soares.lista.database.DBConnection
import com.rodrigo.soares.lista.models.Item
import com.rodrigo.soares.lista.models.Lista
import kotlinx.android.synthetic.main.activity_add_item.*

class AddItemPresenter(val activity: AddItemActivity) {

    fun saveItem(connection: DBConnection, selectedList: Lista){
        val itemDao = ItemDAO(connection)
        itemDao.save(Item(activity.etItemName.text.toString(), activity.etItemPrice.text.toString().toDouble(), selectedList.id, itemDao.getQtByIdLista(selectedList.id!!)))
        activity.finish()
    }
}