package com.example.ankolayoutexample

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.ankolayoutexample.SliderItemFragment.Companion.newInstance

class SliderPagerAdapter(fm: FragmentManager, behavior: Int) :
    FragmentPagerAdapter(fm, behavior) {
    override fun getItem(position: Int): Fragment {
        return newInstance(position)
    }

    // size is hardcoded
    override fun getCount(): Int {
        return 4
    }
}