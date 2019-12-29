package com.example.ankolayoutexample.ui

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView

class MovieUI : AnkoComponent<ViewGroup>{

    companion object{
        val tvTitleId = 1
        val tvYearsId = 2
    }

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        verticalLayout {
            lparams(matchParent , wrapContent)
            padding = dip(16)


            textView {
                id = tvTitleId
                textSize = 16f
                textColor = Color.BLACK
            }.lparams(width = matchParent)

            textView {
                id = tvYearsId
                textSize = 14f
            }.lparams(width = matchParent)
        }
    }

}