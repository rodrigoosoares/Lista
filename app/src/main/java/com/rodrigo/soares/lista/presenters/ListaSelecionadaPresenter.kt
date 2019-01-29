package com.rodrigo.soares.lista.presenters

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.rodrigo.soares.lista.R
import com.rodrigo.soares.lista.activities.ListaSelecionadaActivity
import com.rodrigo.soares.lista.models.Item
import com.rodrigo.soares.lista.models.Lista
import kotlinx.android.synthetic.main.activity_lista_selecionada.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

class ListaSelecionadaPresenter(val activity: ListaSelecionadaActivity) {

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
        Item.adicionarItem(activity.getConnection(), item)
        inputManager?.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
        activity.etNomeItem.setText("")
        if(activity.tvSemItens.visibility == View.VISIBLE)
            activity.tvSemItens.visibility = View.GONE
    }

    fun attListaInfo(lista: Lista){
        activity.supportActionBar?.title = lista.titulo
    }


}