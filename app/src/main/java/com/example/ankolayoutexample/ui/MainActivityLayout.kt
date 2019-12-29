package com.example.ankolayoutexample.ui

import android.view.Gravity
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.ankolayoutexample.MainActivity
import com.example.ankolayoutexample.R
import com.example.ankolayoutexample.ui.imp.ActivityAnkoComponent
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.LayoutParams.*
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.themedToolbar
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.floatingActionButton
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk25.coroutines.onClick

class MainActivityLayout : ActivityAnkoComponent<MainActivity>{
    override lateinit var toolbar: Toolbar

    lateinit var rv_list : RecyclerView
     val frameLayoutId  =2
    companion object{

    }

    override fun createView(ui: AnkoContext<MainActivity>)= with(ui) {
        coordinatorLayout {
            appBarLayout {
                toolbar = themedToolbar(R.style.ThemeOverlay_AppCompat_Dark_ActionBar){
                    backgroundResource = R.color.colorPrimary
                }.lparams(width= matchParent){
                    scrollFlags = SCROLL_FLAG_SNAP or SCROLL_FLAG_SCROLL or SCROLL_FLAG_ENTER_ALWAYS
                }
            }.lparams(width = matchParent)

            verticalLayout {
                rv_list = recyclerView{
                    visibility = View.GONE
                }
                frameLayout {
                    id = frameLayoutId
                }.lparams(matchParent , matchParent)

            }.lparams(width= matchParent){
                behavior = AppBarLayout.ScrollingViewBehavior()
            }

            floatingActionButton {
                imageResource= android.R.drawable.ic_dialog_email
                onClick {
                   ui.owner.showDialog()
                }
            }.lparams{
                margin = dip(10)
                gravity = Gravity.BOTTOM or Gravity.END
            }
        }
    }


}