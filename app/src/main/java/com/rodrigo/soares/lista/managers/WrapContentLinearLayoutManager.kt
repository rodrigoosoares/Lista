package com.rodrigo.soares.lista.managers

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.LinearLayout


class WrapContentLinearLayoutManager(val context: Context) : LinearLayoutManager(context, LinearLayout.VERTICAL, false) {

    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }
}