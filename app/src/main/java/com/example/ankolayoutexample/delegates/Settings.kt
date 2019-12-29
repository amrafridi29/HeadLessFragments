package com.example.ankolayoutexample.delegates

import android.content.Context
import android.preference.PreferenceManager

class Settings (context : Context){
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    var name by prefs.string("Amir")
    var isLoggedIn by prefs.boolean()
}