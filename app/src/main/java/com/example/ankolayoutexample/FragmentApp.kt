package com.example.ankolayoutexample

import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.ankolayoutexample.delegates.Settings
import com.example.ankolayoutexample.delegates.argument
import com.example.ankolayoutexample.delegates.argumentNullable
import com.example.ankolayoutexample.ui.BaseFragment
import com.example.ankolayoutexample.ui.FragmentApUi
import org.jetbrains.anko.cancelButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.yesButton
import com.example.ankolayoutexample.extensions.*
import com.example.ankolayoutexample.statuslistener.*
import org.jetbrains.anko.support.v4.toast


class FragmentApp : BaseFragment<FragmentApUi>() {
    override val ui: FragmentApUi
        get() = FragmentApUi()
    private lateinit var settings: Settings
    private var param1: Int? by argumentNullable()
    private var param2: String by argument()

    companion object {
        fun newInstance(param1: Int, param2: String): FragmentApp =
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



        setOnGpsStatusListener {
            toast(it.toString())
        }

        setOnNetworkStatusListener {
            if (!it) noNetworkDialog()
        }


        onPackageStatusListner {
            toast(it.toString())
        }


        setOnBatteryStatusListener {
            //toast(it.toString())
        }

        setOnTimeTickStatusListener {
            // toast(it.time.toString())
        }

        onPhoneCallStatusListener {
            toast(it.toString())
        }


    }

    private fun noGpsProvider() {
        alert("No Gps Provider Connected", "Gps Provider") {
            yesButton { dialog -> dialog.dismiss() }
            cancelButton { dialog -> dialog.dismiss() }
        }.show()

    }


    fun showResult() {

        hasWriteSettingPermission { result ->
            if (result.canWriteSettings) {
                toast("Granted")
            } else {
                alert("Not Granted Permission", "Write Settings Permission") {
                    positiveButton("Ask Again") { dialog ->
                        result.askAgain()
                        dialog.dismiss()
                    }
                    cancelButton {
                        it.dismiss()
                    }
                }.show()
            }

        }
        hasAccessibilityPermission { result ->
            if (result.hasAccessibility) {

            }
            /* hasOverlayPermission {result->
            if(result.canDrawOverlay){
>>>>>>> Stashed changes
                toast("Granted")
            }else{
                alert("App will not run without usage access permissions." , "Usage Access") {
                    positiveButton("Ask Again"){dialog ->
                        result.askAgain()
                        dialog.dismiss()
                    }
                    cancelButton{
                        it.dismiss()
                    }
                }.show()
            }
        }*/
//        val file  = File("/storage/emulated/0/Download/AdmissionChallan.pdf")
//        file.openFile(requireActivity(), "${BuildConfig.APPLICATION_ID}.provider")
/*        askCameraPermission {
            pickFileFromGallery(FileType.APPLICATIONS, true) {
                when(it.resultCode){
                    Activity.RESULT_OK-> toast(it.data?.data.toString())
                    Activity.RESULT_CANCELED-> toast("Cancel")
                }
            }
        }*/
        }


    }

    private fun noNetworkDialog() {
        alert("No Internet connection available", "Internet Connection") {
            yesButton { dialog -> dialog.dismiss() }
            cancelButton { dialog -> dialog.dismiss() }
        }.show()
    }
}