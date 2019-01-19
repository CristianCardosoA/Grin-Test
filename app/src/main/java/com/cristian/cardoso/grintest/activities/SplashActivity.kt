package com.cristian.cardoso.grintest.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cristian.cardoso.grintest.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val background = object : Thread() {

            override fun run() {

                try {
                    Thread.sleep((5 * 1000).toLong())

                } catch (e: Exception) {

                    e.printStackTrace()

                } finally {

                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }

            }
        }

        background.start()
    }
}
