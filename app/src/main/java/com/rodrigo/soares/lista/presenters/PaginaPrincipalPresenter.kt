package com.rodrigo.soares.lista.presenters

import android.content.Intent
import com.rodrigo.soares.lista.activities.ConfiguracoesActivity
import com.rodrigo.soares.lista.activities.ListaSelecionadaActivity
import com.rodrigo.soares.lista.activities.PaginaPrincipalActivity
import com.rodrigo.soares.lista.fragments.AdicionarListaDialogFragment
import com.rodrigo.soares.lista.models.Lista

class PaginaPrincipalPresenter(var activity: PaginaPrincipalActivity) {

    fun openDialogAddLista(){
        val addListaDialog = AdicionarListaDialogFragment()
        addListaDialog.show(activity.supportFragmentManager, "fragmentAdicionarLista")
    }
    fun toListaSelecionadaActivity(listaSelecionada: Lista){
        val intent = Intent(activity, ListaSelecionadaActivity::class.java)
        intent.putExtra("listaSelecionada", listaSelecionada)
        activity.startActivity(intent)
    }

    fun toConfiguracoesPage() {
        activity.startActivityForResult(Intent(activity, ConfiguracoesActivity::class.java), activity.getRequestCode())
    }
}