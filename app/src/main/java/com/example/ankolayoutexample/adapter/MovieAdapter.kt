package com.example.ankolayoutexample.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ankolayoutexample.datas.Movie
import com.example.ankolayoutexample.ui.MovieUI
import org.jetbrains.anko.AnkoContext

class MovieAdapter (private val list : MutableList<Movie> = mutableListOf()) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(MovieUI().createView(AnkoContext.create(parent.context , parent)))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
       holder.onBind(list[position])
    }


    inner class MovieViewHolder(private val view : View) : RecyclerView.ViewHolder(view){
        var tvTitle: TextView
        var tvYear: TextView

        init {
            tvTitle = itemView.findViewById(MovieUI.tvTitleId)
            tvYear = itemView.findViewById(MovieUI.tvYearsId)
        }

        fun onBind(movie : Movie){
            tvTitle.text = movie.title
            tvYear.text = movie.year.toString()
        }
    }
}