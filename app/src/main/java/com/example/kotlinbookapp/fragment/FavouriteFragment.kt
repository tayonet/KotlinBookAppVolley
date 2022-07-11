package com.example.kotlinbookapp.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.kotlinbookapp.R
import com.example.kotlinbookapp.adapter.RecyclerFavouriteAdapter
import com.example.kotlinbookapp.database.BookDatabase
import com.example.kotlinbookapp.database.BookEntity


class FavouriteFragment : Fragment() {


    lateinit var recyclerFavourite : RecyclerView
    lateinit var progressLayout : RelativeLayout
    lateinit var progressBar : ProgressBar
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var  recyclerAdapter : RecyclerFavouriteAdapter
    var dbBookList = listOf<BookEntity>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_favourite, container, false)

        recyclerFavourite = view.findViewById(R.id.recyclerFavourite)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressbar)


        layoutManager = GridLayoutManager(activity as Context,2)

        dbBookList = RetrieveFavourite(activity as Context).execute().get()

        if(activity != null) {
            progressLayout.visibility = View.GONE
            recyclerAdapter= RecyclerFavouriteAdapter(activity as Context,dbBookList)
            recyclerFavourite.adapter = recyclerAdapter
            recyclerFavourite.layoutManager = layoutManager
        }

        return view


    }

    class RetrieveFavourite(val context : Context) : AsyncTask<Void,Void,List<BookEntity>> () {

        override fun doInBackground(vararg p0: Void?): List<BookEntity> {
            val db = Room.databaseBuilder(context,BookDatabase::class.java,"books-db").build()
            return db.bookDao().getAllBooks()

        }

    }


}