package com.example.calculator

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigateToCalculatorFragment()
        setStatusBarColor()
    }

    private fun navigateToCalculatorFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.container, CalculatorFragment())
            .commit()
    }

    private fun setStatusBarColor() {
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.extra_light_blue)
        }
    }
}