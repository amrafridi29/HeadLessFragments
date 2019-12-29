package com.example.ankolayoutexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ankolayoutexample.adapter.MovieAdapter
import com.example.ankolayoutexample.datas.Movie
import com.example.ankolayoutexample.delegates.Settings
import com.example.ankolayoutexample.extensions.addFragment
import com.example.ankolayoutexample.extensions.setOnNetworkConnectivityListener
import com.example.ankolayoutexample.ui.BaseActivity
import com.example.ankolayoutexample.ui.MainActivityLayout
import org.jetbrains.anko.alert
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.yesButton
import com.example.ankolayoutexample.network.NetworkHelper
import org.jetbrains.anko.cancelButton
import org.jetbrains.anko.support.v4.alert


class MainActivity : BaseActivity<MainActivityLayout>() {

    private lateinit var adapter : MovieAdapter
    override val ui = MainActivityLayout()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val list: MutableList<Movie> = mutableListOf()
        list.add(Movie("Sherlock Holmes",2009))
        list.add(Movie("The Shawshank Redemption",1994))
        list.add(Movie("Forrest Gump",1994))
        list.add(Movie("Titanic",1997))
        list.add(Movie("Taxi",1998))
        list.add(Movie("Inception",1994))
        list.add(Movie("The Imitation Game",2014))
        list.add(Movie("Sherlock Holmes",2009))
        list.add(Movie("The Shawshank Redemption",1994))
        list.add(Movie("Forrest Gump",1994))
        list.add(Movie("Titanic",1997))
        list.add(Movie("Taxi",1998))
        list.add(Movie("Inception",1994))
        list.add(Movie("The Imitation Game",2014))
        list.add(Movie("Sherlock Holmes",2009))
        list.add(Movie("The Shawshank Redemption",1994))
        list.add(Movie("Forrest Gump",1994))
        list.add(Movie("Titanic",1997))
        list.add(Movie("Taxi",1998))
        list.add(Movie("Inception",1994))
        list.add(Movie("The Imitation Game",2014))

        adapter = MovieAdapter(list)

        ui.rv_list.layoutManager = LinearLayoutManager(this)
        ui.rv_list.adapter = adapter

        if(savedInstanceState==null){
            addFragment(FragmentApp.newInstance(12, "Amir") , containerViewId = ui.frameLayoutId)
        }

//        setOnNetworkConnectivityListener {
//            if(!it){
//                noNetworkDialog()
//            }
//        }



    }

    private fun noNetworkDialog() {
        alert("No Internet connection available" , "Internet Connection") {
            yesButton { dialog -> dialog.dismiss() }
            cancelButton { dialog -> dialog.dismiss() }
        }.show()
    }

    fun showDialog(){
        alert("Message" , "title"){
            yesButton { dialog ->
                dialog.dismiss()
            }
        }.show()
    }


}
