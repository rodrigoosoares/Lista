package com.rodrigo.soares.lista.adapters

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rodrigo.soares.lista.R
import com.rodrigo.soares.lista.activities.MainPageActivity
import com.rodrigo.soares.lista.dao.impl.ItemDAO
import com.rodrigo.soares.lista.models.Lista
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row_listas_pagina_principal.view.*
import java.util.*


class ReclyclerViewListasAdapter(val activity: MainPageActivity, val listas: MutableList<Lista>) : RecyclerView.Adapter<ReclyclerViewListasAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_listas_pagina_principal, parent, false)

        return ViewHolder(view, activity, listas)
    }

    override fun getItemCount(): Int = listas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titulo.text = listas[position].titulo
        holder.qtItem.text = ItemDAO(activity.getConnection()).getQtByIdLista(listas[position].id!!).toString()
        //holder.corLista.setBackgroundColor(Color.parseColor(listas[position].corTitulo))
        val drawable = holder.corLista.background as GradientDrawable
        drawable.setColor(Color.parseColor(listas[position].corTitulo))
    }

    fun onItemMove(initialPosition: Int, finalPosition: Int) {
        if(initialPosition < listas.size && finalPosition < listas.size){
            if(initialPosition < finalPosition){
                for (i in initialPosition until finalPosition) {
                    Collections.swap(listas, i, i+1)
                }
            }else{
                for (i in initialPosition..finalPosition + 1){
                    Collections.swap(listas, i, i-1)
                }
            }
        }
        Thread(Runnable {
            val listaMovidaUser = listas[finalPosition]
            val listaMovidaAuto = listas[initialPosition]

            listaMovidaUser.position = finalPosition
            listaMovidaAuto.position = initialPosition
            activity.getListDao()!!.update(listaMovidaUser)
            activity.getListDao()!!.update(listaMovidaAuto)
        }).start()

        notifyItemMoved(initialPosition, finalPosition)
    }

    fun removeItem(position: Int) {
        Snackbar.make(activity.rlPaginaPrincipal, "Deseja excluir essa a lista: ${listas[position].titulo}", 2500)
            .setAction("Excluir") {
                activity.getListDao()!!.remove(listas[position].id!!.toInt())
                listas.removeAt(position)
                notifyItemRemoved(position)
            }.addCallback(object: Snackbar.Callback(){
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    notifyItemChanged(position)
                }
            }).show()
    }

    class ViewHolder(view: View, activity: MainPageActivity, listas: MutableList<Lista>) : RecyclerView.ViewHolder(view) {
        val titulo = view.tvNomeListaRow
        val qtItem = view.tvQtItemListaRow
        val corLista = view.ivListaCor

        init {
            view.setOnClickListener {
                activity.getPresenter()?.toSelectedListActivity(listas[adapterPosition])
            }
        }

    }

}