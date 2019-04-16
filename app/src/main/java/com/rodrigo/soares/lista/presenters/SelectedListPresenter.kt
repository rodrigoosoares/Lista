package com.rodrigo.soares.lista.presenters

import android.content.Context
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.rodrigo.soares.lista.R
import com.rodrigo.soares.lista.activities.SelectedListActivity
import com.rodrigo.soares.lista.dao.impl.ItemDAO
import com.rodrigo.soares.lista.models.Item
import com.rodrigo.soares.lista.models.Lista
import kotlinx.android.synthetic.main.activity_lista_selecionada.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

class SelectedListPresenter(val activity: SelectedListActivity) {

    var inputManager: InputMethodManager? = null

    fun openKeyBoard(){
        inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun toggleViewsVisibilityEvent(){
        KeyboardVisibilityEvent.setEventListener(activity) {
            if(activity.etNomeItem.visibility == View.VISIBLE && activity.btnAddItem.visibility == View.VISIBLE) {
                activity.fabAddItem.show()
                activity.etNomeItem.visibility = View.INVISIBLE
                activity.btnAddItem.visibility = View.INVISIBLE
            }
            else if(activity.etNomeItem.visibility == View.INVISIBLE && activity.btnAddItem.visibility == View.INVISIBLE){
                activity.fabAddItem.hide()
                activity.etNomeItem.visibility = View.VISIBLE
                activity.btnAddItem.visibility = View.VISIBLE
                activity.etNomeItem.requestFocus()
            }
        }
    }

    fun addItem(item: Item){
        if(item.titulo=="")
            item.titulo = activity.resources.getString(R.string.novo_item_branco)
        activity.getItemDAO()!!.save(item)
        inputManager?.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
        activity.etNomeItem.setText("")
    }

    fun getAllItems(itemDao: ItemDAO, selectedList: Lista) = itemDao.getAllByIdLista(selectedList.id!!)

    fun attListaInfo(lista: Lista){
        activity.supportActionBar?.title = lista.titulo
    }

    fun etNomeItemOnKeyEvent(keyCode: Int, keyEvent: KeyEvent): Boolean{
        if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
            addItem(Item(activity.etNomeItem.text.toString().trim(), "", activity.getSelectedList().id))
            activity.attListItems()
            return true
        }
        return false
    }


}