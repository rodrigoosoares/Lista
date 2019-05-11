package com.rodrigo.soares.lista.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.rodrigo.soares.lista.R
import com.rodrigo.soares.lista.database.DBConnection

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        if(dataBaseExist()) {
            Handler().postDelayed({
                toMainPageActivity()
            }, 3000)
        } else {
            createDatabase()
        }

    }

    private fun createDatabase() {
        val connection = DBConnection(this)
        connection.close()
    }

    private fun toMainPageActivity() {
        startActivity(Intent(this, MainPageActivity::class.java))
        finish()
    }

    fun dataBaseExist(): Boolean {
        val dbFIle = this.getDatabasePath(DBConnection.DATABSE_NAME)
        return dbFIle.exists()
    }
}
