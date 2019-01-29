package com.rodrigo.soares.lista.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.rodrigo.soares.lista.R
import com.rodrigo.soares.lista.activities.PaginaPrincipalActivity
import com.rodrigo.soares.lista.models.Item
import com.rodrigo.soares.lista.models.Lista
import kotlinx.android.synthetic.main.row_listas_pagina_principal.view.*

class ListViewListasAdapter(val activity: PaginaPrincipalActivity, var listas: MutableList<Lista>) : ArrayAdapter<Lista>(activity, R.layout.row_listas_pagina_principal, listas) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder
        if(convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.row_listas_pagina_principal, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        }else{
            view = convertView
            holder = view.tag as ViewHolder
        }
        holder.titulo.text = listas[position].titulo
        holder.qtItem.text = Item.getItemsQtByIdLista(activity.getConnection(), listas[position].id!!).toString()
        holder.corLista.setBackgroundColor(Color.parseColor(listas[position].corTitulo))

        return view
    }

    class ViewHolder(view: View){
        val titulo = view.tvNomeListaRow
        val qtItem = view.tvQtItemListaRow
        val corLista = view.ivListaCor
    }

}