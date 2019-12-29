package com.example.ankolayoutexample.ui

import android.view.View
import com.example.ankolayoutexample.FragmentApp
import com.example.ankolayoutexample.ui.imp.FragmentAnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.button
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.verticalLayout

class FragmentApUi : FragmentAnkoComponent<FragmentApp>{
    override fun createView(ui: AnkoContext<FragmentApp>) = with(ui) {
        verticalLayout {
            button("Click Me") {
                onClick {
                    ui.owner.showResult()
                }
            }
        }
    }

}