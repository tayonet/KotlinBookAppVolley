package com.example.kotlinbookapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinbookapp.R
import com.example.kotlinbookapp.database.BookEntity
import com.squareup.picasso.Picasso

class RecyclerFavouriteAdapter(val context : Context,val bookList : List<BookEntity>) : RecyclerView.Adapter<RecyclerFavouriteAdapter.FavouriteViewHolder>(){

    class FavouriteViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val bookName : TextView = view.findViewById(R.id.favBookTitle)
        val bookAuthor : TextView = view.findViewById(R.id.favBookAuthor)
        val bookPrice : TextView = view.findViewById(R.id.favBookPrice)
        val bookRating : TextView = view.findViewById(R.id.favBookRating)
        val bookImage : ImageView = view.findViewById(R.id.favBookImage)
        val booksContainer :  LinearLayout = view.findViewById(R.id.favContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_favourite_row,parent,false)
            return FavouriteViewHolder(view)

    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
            val book = bookList[position]
             holder.bookName.text = book.bookName
             holder.bookAuthor.text = book.bookAuthor
             holder.bookPrice.text = book.bookPrice
             holder.bookRating.text = book.bookRating
             Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.bookImage)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

}