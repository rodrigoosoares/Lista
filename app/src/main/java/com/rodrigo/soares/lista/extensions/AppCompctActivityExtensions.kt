package com.rodrigo.soares.lista.extensions

import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import com.rodrigo.soares.lista.R

fun AppCompatActivity.setNightMode(){
    if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
        setTheme(R.style.NightAppTheme)
    else
        setTheme(R.style.AppTheme)
}