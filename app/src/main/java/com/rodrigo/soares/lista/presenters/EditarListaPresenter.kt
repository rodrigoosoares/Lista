package com.rodrigo.soares.lista.presenters

import com.rodrigo.soares.lista.activities.EditarListaActivity
import com.rodrigo.soares.lista.models.Lista

class EditarListaPresenter(val activity: EditarListaActivity) {

    fun salvarListaAlteracoes(lista: Lista){
        activity.getListaDao()!!.update(lista)
    }
}