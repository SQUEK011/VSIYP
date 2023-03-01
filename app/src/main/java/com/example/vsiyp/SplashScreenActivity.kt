package com.example.vsiyp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        startSplash()
    }

    private fun startSplash(){
        Handler().postDelayed({ //This method will be executed once the timer is over
            // Start your app main activity
            val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(intent)
            // close this activity
            finish()
        }, 3000)
    }






}