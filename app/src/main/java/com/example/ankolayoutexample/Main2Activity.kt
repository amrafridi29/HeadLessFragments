package com.example.ankolayoutexample

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.ankolayoutexample.ui.BaseActivity
import com.example.ankolayoutexample.ui.ConstraintActivityUi

import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : BaseActivity<ConstraintActivityUi>() {
    override val ui: ConstraintActivityUi
        get() = ConstraintActivityUi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


}
