package com.rodrigo.soares.lista.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatDelegate
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.rodrigo.soares.lista.R
import com.rodrigo.soares.lista.activities.MainPageActivity
import com.rodrigo.soares.lista.adapters.SpinnerColorsAdapter
import com.rodrigo.soares.lista.models.Lista

class AddListDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            activity?.setTheme(R.style.NightAppTheme)
        else
            activity?.setTheme(R.style.AppTheme)
        val builder = AlertDialog.Builder(context)
        val inflater = activity?.layoutInflater
        val layout = inflater?.inflate(R.layout.fragment_custom_dialog, null)
        builder.setView(layout)
        val btnCreateList = layout?.findViewById<Button>(R.id.btnCreateList)
        val spColors = layout?.findViewById<Spinner>(R.id.spColors)
        btnCreateList?.setOnClickListener {
            val listName = layout.findViewById<EditText>(R.id.etListName).text.toString()
            (activity as MainPageActivity).getListDao()!!.save(Lista(listName, spColors?.selectedItem.toString()))
            dismiss()
        }
        val spinnerAdapter = SpinnerColorsAdapter(context)
        spColors?.adapter = spinnerAdapter
        return builder.create()
    }

    override fun onDismiss(dialog: DialogInterface?) {
        (activity as MainPageActivity).updateLists()
    }

    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)
        dismiss()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}
