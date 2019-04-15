package com.rodrigo.soares.lista.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.view.MenuItem
import com.rodrigo.soares.lista.R
import kotlinx.android.synthetic.main.activity_configuracoes.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class ConfiguracoesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.NightAppTheme)
        else
            setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracoes)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Configurações"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            swModoEscuro.isChecked = true

        swModoEscuro.setOnCheckedChangeListener{_, isChecked ->
            if (isChecked)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            restartApp()
        }
    }

    private fun restartApp() {
        recreate()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home ->{
                if(swModoEscuro.isChecked) {
                    val resultIntent = Intent()
                    setResult(Activity.RESULT_OK, resultIntent)
                }
                onBackPressed()
                return true
            }
        }
        return false
    }
}
