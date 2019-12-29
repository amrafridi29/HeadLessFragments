package com.example.ankolayoutexample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ankolayoutexample.ui.imp.FragmentAnkoComponent
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext

abstract class BaseFragment <out UI : FragmentAnkoComponent<out Fragment>>: Fragment(){
    abstract val ui : UI

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        (ui as FragmentAnkoComponent<Fragment>).createView(AnkoContext.create(requireContext() ,this))
}