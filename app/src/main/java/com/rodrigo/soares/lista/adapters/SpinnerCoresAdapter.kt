package com.rodrigo.soares.lista.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.rodrigo.soares.lista.R
import com.rodrigo.soares.lista.models.Cores
import kotlinx.android.synthetic.main.row_spinner_cores.view.*

class SpinnerCoresAdapter(context: Context?): ArrayAdapter<String>(context!!, R.layout.row_spinner_cores, Cores.cores){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        if (convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.row_spinner_cores, parent, false)
        }else{
            view = convertView
        }
        view.tvCor.setBackgroundColor(Color.parseColor(Cores.cores[position]))
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }
}