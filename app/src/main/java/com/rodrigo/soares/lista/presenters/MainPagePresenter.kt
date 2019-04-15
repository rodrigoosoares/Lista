package com.rodrigo.soares.lista.presenters

import android.content.Intent
import com.rodrigo.soares.lista.activities.ConfiguracoesActivity
import com.rodrigo.soares.lista.activities.MainPageActivity
import com.rodrigo.soares.lista.activities.SelectedListActivity
import com.rodrigo.soares.lista.fragments.AdicionarListaDialogFragment
import com.rodrigo.soares.lista.models.Lista

class MainPagePresenter(var activity: MainPageActivity) {

    private val FRAGMENT_TAG = "fragmentAdicionarLista"
    private val SELECTED_LIST_EXTRA = "listaSelecionada"

    fun openDialogAddList(){
        val addListDialog = AdicionarListaDialogFragment()
        addListDialog.show(activity.supportFragmentManager, FRAGMENT_TAG)
    }
    fun toSelectedListActivity(listaSelecionada: Lista){
        val intent = Intent(activity, SelectedListActivity::class.java)
        intent.putExtra(SELECTED_LIST_EXTRA, listaSelecionada)
        activity.startActivity(intent)
    }

    fun toConfigPage() {
        activity.startActivityForResult(Intent(activity, ConfiguracoesActivity::class.java), activity.getRequestCode())
    }
}