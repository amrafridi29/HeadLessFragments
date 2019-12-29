package com.example.ankolayoutexample.ui.imp

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import org.jetbrains.anko.AnkoComponent

interface ActivityAnkoComponent <T : AppCompatActivity> : AnkoComponent<T>{
    val toolbar : Toolbar
}