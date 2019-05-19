package com.example.myapplicationtimer

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.util.SparseArray
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.main_application.*
import java.lang.StringBuilder
import kotlin.math.roundToInt

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainApplication : AppCompatActivity() {

    private lateinit var timer: CountDownTimer

    enum class TimerState{
        Start, Stop
    }

    private var timerState = TimerState.Start

    var seconds : Int = 0
    val time : Long = 1001
    val timeInterval : Long = 1000

    private lateinit var STOP : String

    private lateinit var START : String

    private val secondKey : String = "TEXT_SECONDS"
    private val buttonKey : String = "TEXT_BUTTON"
    private val textKey : String = "TEXT_TEXTVIEW"

    private val numbers: SparseArray<String> = SparseArray()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_application)

        defineNames()

        fillNumbers(numbers)

        val button : Button = findViewById(R.id.button)
        setCastomButton(button)
    }



    override fun onResume() {
        super.onResume()

        val allTime : Long = (time - seconds) * 1000
        timer = object : CountDownTimer(allTime, timeInterval) {

            var temp : StringBuilder = StringBuilder()

            override fun onTick(milSeconds: Long) {
                val textView : TextView = findViewById(R.id.text)
                seconds += 1
                if (seconds > time) {
                    this.onFinish()
                    this.cancel()
                }

                temp.append(numbers.get(seconds, ""))

                if (temp.isEmpty()) {
                    parseNumber(temp)
                }

                textView.text = temp.toString()
                temp.delete(0, temp.length)
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                val button : Button = findViewById(R.id.button)
                button.text = START
                timerState = TimerState.Start
                temp.delete(0, temp.length)
                seconds = 0
            }

        }

        if (timerState === TimerState.Stop) {
            timer.start()
        }
    }




    override fun onPause() {
        super.onPause()
        timer.cancel()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val button : Button = findViewById(R.id.button)
        val textView : TextView = findViewById(R.id.text)

        outState.putInt(secondKey, seconds)
        outState.putString(buttonKey, button.text.toString())
        outState.putString(textKey, textView.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        val textView : TextView = findViewById(R.id.text)

        seconds = savedInstanceState.getInt(secondKey)
        val textStr : String = savedInstanceState.getString(textKey, "")
        val buttonStr : String = savedInstanceState.getString(buttonKey)

        timerState = if (buttonStr == START) {
            TimerState.Start
        } else {
            TimerState.Stop
        }

        textView.text = textStr
        button.text = buttonStr
    }

    private fun fillNumbers(numbers : SparseArray<String>) {

        val numberStr = resources.getStringArray(R.array.number_strings)

        val num  = resources.getIntArray(R.array.numbers)

        var i = 0
        for (str in numberStr) {
            numbers.append(num[i], str)
            i += 1
        }
    }

    fun parseNumber (buff : StringBuilder) {

        var curNum = seconds
        var i = 0
        var n : Int

        while (curNum > 0) {
            n = curNum % 10
            curNum /= 10

            if (n != 0) {
                if (i == 0 && curNum % 10 == 1) {
                    buff.insert(0, numbers.get(n + 10) + " ")
                    curNum /= 10
                    i += 1
                } else {
                    val p: Int = Math.pow(10.0, i.toDouble()).roundToInt()
                    buff.insert(0, numbers.get(n * p) + " ")
                }
            }
            i += 1
        }
    }

    fun defineNames () {
        START = getString(R.string.start)
        STOP = getString(R.string.stop)
    }

    @SuppressLint("SetTextI18n")
    fun setCastomButton (button: Button) {
        button.text = "START"
        timerState = TimerState.Start

        button.setOnClickListener {

            if (timerState == TimerState.Start) {
                button.text = STOP
                timerState = TimerState.Stop
                timer.start()

            } else if (timerState == TimerState.Stop) {
                button.text = STOP
                timerState = TimerState.Start
                timer.cancel()
            }
        }
    }

}


