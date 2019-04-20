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


class ReclyclerViewListsAdapter(val activity: MainPageActivity, val lists: MutableList<Lista>) : RecyclerView.Adapter<ReclyclerViewListsAdapter.ViewHolder>() {

    private val REMOVE_TEXT_UNDO = "DESFAZER"
    private val REMOVE_LIST_SNACKBAR_TEXT = " excluída"

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_listas_pagina_principal, parent, false)

        return ViewHolder(view, activity, lists)
    }

    override fun getItemCount(): Int = lists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = lists[position].titulo
        holder.qtItem.text = ItemDAO(activity.getConnection()).getQtByIdLista(lists[position].id!!).toString()
        //holder.colorList.setBackgroundColor(Color.parseColor(lists[position].corTitulo))
        val drawable = holder.colorList.background as GradientDrawable
        drawable.setColor(Color.parseColor(lists[position].corTitulo))
    }

    fun onItemMove(initialPosition: Int, finalPosition: Int) {
        if(initialPosition < lists.size && finalPosition < lists.size){
            if(initialPosition < finalPosition){
                for (i in initialPosition until finalPosition) {
                    Collections.swap(lists, i, i+1)
                }
            }else{
                for (i in initialPosition..finalPosition + 1){
                    Collections.swap(lists, i, i-1)
                }
            }
        }
        Thread(Runnable {
            val listMovedByUser = lists[finalPosition]
            val listMovedAuto = lists[initialPosition]

            listMovedByUser.position = finalPosition
            listMovedAuto.position = initialPosition
            activity.getListDao()!!.update(listMovedByUser)
            activity.getListDao()!!.update(listMovedAuto)
        }).start()

        notifyItemMoved(initialPosition, finalPosition)
    }

    fun removeItem(position: Int) {
        Snackbar.make(activity.rlPaginaPrincipal, lists[position].titulo + REMOVE_LIST_SNACKBAR_TEXT, Snackbar.LENGTH_LONG)
            .setAction(REMOVE_TEXT_UNDO) {
                notifyItemChanged(position)
            }.addCallback(object: Snackbar.Callback(){
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    if (event == DISMISS_EVENT_TIMEOUT) {
                        activity.getListDao()!!.remove(lists[position].id!!.toInt())
                        lists.removeAt(position)
                        notifyItemRemoved(position)
                    }
                }
            }).show()
    }

    class ViewHolder(view: View, activity: MainPageActivity, lists: MutableList<Lista>) : RecyclerView.ViewHolder(view) {
        val title = view.tvNomeListaRow
        val qtItem = view.tvQtItemListaRow
        val colorList = view.ivListaCor

        init {
            view.setOnClickListener {
                activity.getPresenter()?.toSelectedListActivity(lists[adapterPosition])
            }
        }
    }
}