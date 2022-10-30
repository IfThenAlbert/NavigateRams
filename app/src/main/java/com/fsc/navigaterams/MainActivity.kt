package com.fsc.navigaterams

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // for a SPLASH SCREEN
        // as soon as the app opens, it will go to this screen and will wait for 3 seconds then will go to the actual main screen
        @Suppress("DEPRECATION")
        Handler().postDelayed(Runnable {
            Toast.makeText(applicationContext,"HELLO THERE!",Toast.LENGTH_LONG).show() // greet the user
            startActivity(Intent(applicationContext,MainScreen::class.java))
        },2000)
    }
}