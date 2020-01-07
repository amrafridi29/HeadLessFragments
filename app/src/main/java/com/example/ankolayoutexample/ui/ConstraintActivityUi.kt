package com.example.ankolayoutexample.ui

import android.os.Build
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.*
import com.example.ankolayoutexample.Main2Activity
import com.example.ankolayoutexample.R
import com.example.ankolayoutexample.ui.imp.ActivityAnkoComponent
import com.google.android.material.appbar.AppBarLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.themedToolbar
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.constraint.layout.ConstraintSetBuilder
import org.jetbrains.anko.constraint.layout.applyConstraintSet
import org.jetbrains.anko.constraint.layout.constraintLayout
import org.jetbrains.anko.constraint.layout.constraintSet

class ConstraintActivityUi : ActivityAnkoComponent<Main2Activity>{
    override lateinit var toolbar: Toolbar

    override fun createView(ui: AnkoContext<Main2Activity>) = with(ui) {
        constraintLayout {
            toolbar = themedToolbar(R.style.ThemeOverlay_AppCompat_Dark_ActionBar){
                id = View.generateViewId()
                backgroundResource = R.color.colorPrimary
            }.lparams(width= matchParent){
               // scrollF = AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP or AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
            }
            val btn = button{
                id = View.generateViewId()
            }.lparams(ConstraintLayout.LayoutParams.WRAP_CONTENT , ConstraintLayout.LayoutParams.WRAP_CONTENT){

            }

            val img = imageView{
                id = View.generateViewId()
                imageResource = R.mipmap.ic_launcher
            }.lparams(dip(40) ,dip(40)){

            }



            constraintSet {

                connect(toolbar.id, TOP, PARENT_ID, TOP)
                connect(toolbar.id, LEFT, PARENT_ID, LEFT)
                connect(toolbar.id, RIGHT, PARENT_ID, RIGHT)

                connect(btn.id, TOP, toolbar.id, BOTTOM)
                connect(btn.id , LEFT , PARENT_ID , LEFT)
                connect(btn.id , RIGHT , PARENT_ID , RIGHT)

                connect(img.id , TOP , btn.id , BOTTOM)
                connect(img.id , LEFT , btn.id , LEFT)
                connect(img.id , RIGHT , btn.id , RIGHT)
            }.applyTo(this)
        }
    }

}