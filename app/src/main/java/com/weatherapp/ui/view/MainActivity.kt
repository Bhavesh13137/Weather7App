package com.weatherapp.ui.view

import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.weatherapp.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun showMessage(msg: String) {
        val parentLayout = findViewById<View>(android.R.id.content)
        val snackBar = Snackbar.make(parentLayout, msg, Snackbar.LENGTH_LONG).setAction("Action", null)
        snackBar.setActionTextColor(Color.RED)
        val snackBarView = snackBar.view
        val textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        textView.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        textView.textSize = 14f
        textView.typeface.isBold
        snackBar.show()
    }

}