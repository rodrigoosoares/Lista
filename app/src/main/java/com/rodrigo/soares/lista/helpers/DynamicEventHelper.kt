package com.rodrigo.soares.lista.helpers

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

class DynamicEventHelper(val callback: DynamicEventsCallback) : ItemTouchHelper.Callback() {

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(p0: RecyclerView, p1: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.END or ItemTouchHelper.START
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(p0: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        callback.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        callback.removeItem(viewHolder.adapterPosition)
    }

    interface DynamicEventsCallback{
        fun onItemMove(InitialPosition: Int, FinalPosition: Int)
        fun removeItem(position: Int)
    }
}