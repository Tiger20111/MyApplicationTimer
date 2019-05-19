package com.example.myapplicationtimer

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        timer = object : CountDownTimer(waitTime, 1000) {
            override fun onTick(milSeconds: Long) {
                waitTime = milSeconds
                textView = findViewById<TextView>(R.id.text_enot)
                val oldText = textView.text
                var newText = "hm"
                if (countTick == 2) {
                    countTick = 0
                    newText = baseText
                } else {
                    newText = "$oldText."
                }
                textView.text = newText
                countTick++
            }

            override fun onFinish() {
                val intent = Intent(this@MainActivity, MainApplication::class.java)
                startActivity(intent)
                finish()
            }

        }.start()
    }

    override fun onPause() {
        super.onPause()
        timer.cancel()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        waitTime = savedInstanceState.getLong(timeSave)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(timeSave, waitTime)
    }


    private var waitTime: Long = 4000
    private val timeSave: String = ""
    private lateinit var timer: CountDownTimer
    lateinit var textView : TextView
    private var countTick = 0
    private var baseText: String = "The application is loading, please, wait."
}
