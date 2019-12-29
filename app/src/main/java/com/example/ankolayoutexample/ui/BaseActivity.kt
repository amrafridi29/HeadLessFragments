package com.example.ankolayoutexample.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ankolayoutexample.ui.imp.ActivityAnkoComponent
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.setContentView


abstract class BaseActivity<out UI : ActivityAnkoComponent<out AppCompatActivity>> : AppCompatActivity(){
    abstract val ui : UI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (ui as ActivityAnkoComponent<AppCompatActivity>).setContentView(this)
        setSupportActionBar(ui.toolbar)
    }

}
