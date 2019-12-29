package com.example.ankolayoutexample

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ankolayoutexample.delegates.FragmentArgumentDelegate
import com.example.ankolayoutexample.delegates.Settings
import com.example.ankolayoutexample.delegates.argument
import com.example.ankolayoutexample.delegates.argumentNullable
import com.example.ankolayoutexample.ui.BaseFragment
import com.example.ankolayoutexample.ui.FragmentApUi
import com.example.ankolayoutexample.ui.MovieUI
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.alert
import org.jetbrains.anko.cancelButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.yesButton
import android.content.Intent
import com.example.ankolayoutexample.activityResult.FileType
import com.example.ankolayoutexample.extensions.*


class FragmentApp : BaseFragment<FragmentApUi>(){
    override val ui: FragmentApUi
        get() = FragmentApUi()
    private lateinit var settings : Settings
    private var param1 : Int? by argumentNullable()
    private var param2 : String by argument()

    companion object{
        fun newInstance(param1 : Int , param2 : String) : FragmentApp=
            FragmentApp().apply {
                this.param1 = param1
                this.param2 = param2
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = Settings(requireContext())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setOnNetworkConnectivityListener {
            if(!it) noNetworkDialog()
        }
    }


    fun showResult(){
        askCameraPermission {
            pickFileFromGallery(FileType.APPLICATIONS, true) {
                when(it.resultCode){
                    Activity.RESULT_OK-> toast(it.data?.data.toString())
                    Activity.RESULT_CANCELED-> toast("Cancel")
                }
            }
        }
    }

    private fun noNetworkDialog() {
        alert("No Internet connection available" , "Internet Connection") {
            yesButton { dialog -> dialog.dismiss() }
            cancelButton { dialog -> dialog.dismiss() }
        }.show()
    }
}