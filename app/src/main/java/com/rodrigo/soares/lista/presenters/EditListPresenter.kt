package com.rodrigo.soares.lista.presenters

import android.app.Activity
import android.content.Intent
import com.rodrigo.soares.lista.activities.EditListActivity
import com.rodrigo.soares.lista.models.Lista

class EditListPresenter(val activity: EditListActivity) {

    fun salvarListaAlteracoes(lista: Lista){
        activity.getListaDao()!!.update(lista)
    }

    fun btnSavedEditedListOnClickListener(editedList: Lista){
        val resultIntent = Intent()
        resultIntent.putExtra(EditListActivity.EDITED_LIST_EXTRA_STRING, editedList)
        activity.setResult(Activity.RESULT_OK, resultIntent)
        salvarListaAlteracoes(editedList)
        activity.finish()
    }
}