package com.example.kotlinbookapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinbookapp.R
import com.example.kotlinbookapp.activity.DescriptionActivity
import com.example.kotlinbookapp.model.Book
import com.squareup.picasso.Picasso

// create primary constructor for the class
class RecyclerDashboardAdapter(val context : Context, val itemList : ArrayList<Book>) : RecyclerView.Adapter<RecyclerDashboardAdapter.DashboardViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_dashboard_row,parent,false)
        return DashboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val book  = itemList.get(position)
        // bind the item to the view in the view holder
        holder.txt_book_name.text = book.bookName
        holder.txt_author.text = book.bookAuthor
        holder.txt_price.text = book.bookPrice
        holder.txt_rating.text = book.bookRating
      //  holder.book_cover.setImageResource(item.bookImage)
        Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.book_cover)


        // handle the click event on the items
        holder.parent_view.setOnClickListener{
           // Toast.makeText(context,"You clicked on ${holder.txt_book_name.text}",Toast.LENGTH_SHORT).show()

            // navigate to the Description activity when an item on the recyclerview is clicked

            val intent = Intent(context,DescriptionActivity::class.java)
            // put the book id in the intent
            intent.putExtra("book_id",book.bookId)
            context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {

        return itemList.size
    }

    // create the view holder inner class
    class DashboardViewHolder(view : View) : RecyclerView.ViewHolder(view ){

        val txt_book_name : TextView = view.findViewById(R.id.book_name)
        val txt_author : TextView = view.findViewById(R.id.author)
        val txt_price : TextView = view.findViewById(R.id.price)
        val txt_rating : TextView = view.findViewById(R.id.ratings)
        val book_cover : ImageView = view.findViewById(R.id.book_cover)
        val parent_view : RelativeLayout  = view.findViewById(R.id.recyler_parent_view)
    }

}