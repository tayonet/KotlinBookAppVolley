package com.example.kotlinbookapp.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.kotlinbookapp.R
import com.example.kotlinbookapp.database.BookDatabase
import com.example.kotlinbookapp.database.BookEntity
import com.example.kotlinbookapp.util.ConnectionManager
import com.squareup.picasso.Picasso
import org.json.JSONObject

class DescriptionActivity : AppCompatActivity() {

    // get the id stored in the intent object
    var bookId : String? = "100"

    // get references to the views

    lateinit var  bookName : TextView
    lateinit var bookAuthor : TextView
    lateinit var  bookPrice : TextView
    lateinit var  bookRating :  TextView
    lateinit var  progressbar : ProgressBar
    lateinit var progressLayout : RelativeLayout
    lateinit var bookDesc : TextView
    lateinit var  bookImage : ImageView
    lateinit var btnAddToFav : Button

    lateinit var  toolbar: Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        // initialise references

        bookName = findViewById(R.id.bookName)
        bookAuthor = findViewById(R.id.bookAuthor)
        bookPrice = findViewById(R.id.bookPrice)
        bookRating = findViewById(R.id.bookRating)

        progressbar = findViewById(R.id.progressbar)
        progressbar.visibility = View.VISIBLE

        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE

        bookDesc  = findViewById(R.id.bookDesc)
        bookImage = findViewById(R.id.bookImage)
        btnAddToFav = findViewById(R.id.btnAddToFav)

        // instantiate the toolbar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book Details"


        if(intent != null ) {
            bookId = intent.getStringExtra("book_id")


            } else {
                finish()
                Toast.makeText(this@DescriptionActivity,"Some unexpected error occurred!",Toast.LENGTH_SHORT).show()
            }
            if(bookId == "100") {
                finish()
                Toast.makeText(this@DescriptionActivity,"Some unexpected error occurred!",Toast.LENGTH_SHORT).show()
            }

            val queue = Volley.newRequestQueue(this@DescriptionActivity)
            val url = "http://13.235.250.119/v1/book/get_book/"
            val jsonParams = JSONObject()
            jsonParams.put("book_id",bookId)


            if(ConnectionManager().checkConnectivity(this@DescriptionActivity)) {


                val jsonRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {

                    try {
                        val success = it.getBoolean("success")
                        if (success) {
                            val bookJsonObject = it.getJSONObject("book_data")
                            progressLayout.visibility = View.GONE

                            val bookImageUrl = bookJsonObject.getString("image")
                            Picasso.get().load(bookJsonObject.getString("image")).error(R.drawable.default_book_cover).into(bookImage)
                            bookName.text = bookJsonObject.getString("name")
                            bookAuthor.text = bookJsonObject.getString("author")
                            bookPrice.text = bookJsonObject.getString("price")
                            bookRating.text = bookJsonObject.getString("rating")
                            bookDesc.text = bookJsonObject.getString("description")

                            val bookEntity = BookEntity(
                                    bookId?.toInt() as Int,
                                    bookName.toString(),
                                    bookAuthor.toString(),
                                    bookPrice.toString(),
                                    bookRating.toString(),
                                    bookDesc.toString(),
                                    bookImageUrl
                            )

                            // check if book is in favourite
                            val checkFav = DBAsyncTask(applicationContext,bookEntity,1).execute()
                            val isFav = checkFav.get()

                            if(isFav) {
                                btnAddToFav.text = "Remove from favourite"
                                val favColor = ContextCompat.getColor(applicationContext,R.color.color_favourite)
                                btnAddToFav.setBackgroundColor(favColor)
                            } else {
                                btnAddToFav.text = "Add to favourite"
                                val favColor = ContextCompat.getColor(applicationContext,R.color.colorPrimary)
                            }

                            btnAddToFav.setOnClickListener{
                                // if book is not in favourite yet, try and store the book in favourite using the mode 2
                                if(!DBAsyncTask(applicationContext,bookEntity,1).execute().get()) {
                                    val asycnInsert = DBAsyncTask(applicationContext,bookEntity,2).execute()
                                    val result = asycnInsert.get() // stores true if book is saved as favourite , false otherwise
                                    if(result) {
                                        val alertDialog  = AlertDialog.Builder(applicationContext as Context)
                                        alertDialog.setTitle("Success")
                                        alertDialog.setMessage("Book added to favourite")
                                        alertDialog.setPositiveButton("Ok"){text,listener-> {

                                        }}
                                        alertDialog.setNegativeButton("Cancel"){text,listener ->{

                                        }}
                                        alertDialog.create()
                                        alertDialog.show()
                                        // change the text and background color of the button
                                        btnAddToFav.text = "Remove from favourite"
                                        val favColor = ContextCompat.getColor(applicationContext,R.color.color_favourite)
                                        btnAddToFav.setBackgroundColor(favColor)
                                    } else {
                                        val alertDialog  = AlertDialog.Builder(applicationContext as Context)
                                        alertDialog.setTitle("Error")
                                        alertDialog.setMessage("Book was not added to favourite")
                                        alertDialog.setPositiveButton("Ok"){text,listener-> {

                                        }}
                                        alertDialog.create()
                                        alertDialog.show()
                                    }

                                } else {
                                    val asyncDelete = DBAsyncTask(applicationContext,bookEntity,3).execute()
                                    val result = asyncDelete.get()
                                    if(result) {
                                        val alertDialog  = AlertDialog.Builder(applicationContext as Context)
                                        alertDialog.setTitle("Success")
                                        alertDialog.setMessage("Book removed from favourite")
                                        alertDialog.setPositiveButton("Ok"){text,listener-> {

                                        }}
                                        alertDialog.create()
                                        alertDialog.show()
                                        // change the text and background color of the button
                                        btnAddToFav.text = "Add to favourite"
                                        val favColor = ContextCompat.getColor(applicationContext,R.color.colorPrimary)
                                        btnAddToFav.setBackgroundColor(favColor)

                                    } else {
                                        val alertDialog  = AlertDialog.Builder(applicationContext as Context)
                                        alertDialog.setTitle("Error")
                                        alertDialog.setMessage("Unable to remove book from favourite")
                                        alertDialog.setPositiveButton("Ok"){text,listener-> {

                                        }}
                                        alertDialog.create()
                                        alertDialog.show()
                                        // change the text and background color of the button
                                        btnAddToFav.text = "Remove from favourite"
                                        val favColor = ContextCompat.getColor(applicationContext,R.color.color_favourite)
                                        btnAddToFav.setBackgroundColor(favColor)
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(this, "Some Error Occurred!", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this, "Some Error Occurred!", Toast.LENGTH_SHORT).show()
                    }

                }, Response.ErrorListener {

                    Toast.makeText(this, "Volley error $it", Toast.LENGTH_SHORT).show()
                }) {

                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "9bf534118365f1"
                        return headers
                    }
                }
                queue.add(jsonRequest)
            } else {
                // Internet is not available
                val dialog = AlertDialog.Builder(this@DescriptionActivity as Context)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection is not  Found")
                dialog.setPositiveButton("Open Settings") { text, listener ->

                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    finish()

                }
                dialog.setNegativeButton("Exit") { text, listener ->

                    ActivityCompat.finishAffinity(this@DescriptionActivity)
                }
                dialog.create()
                dialog.show()
            }
        }


            // create an inner class to sublcass AsyncTask class

            class DBAsyncTask(val context : Context,val bookEntity: BookEntity, val mode : Int) : AsyncTask<Void,Void,Boolean> ()  {

                // instantiate the room database
                val db = Room.databaseBuilder(context,BookDatabase::class.java,"books-db").build()

                override fun doInBackground(vararg p0: Void?): Boolean {

                    /*

                    Mode 1 - Check database whether the book is in favourite or not
                    Mode 2 - Save the book inside DB as favourite
                    Mode 3 - Remove book from favourite
                     */

                    when(mode) {
                        1 ->  {
                            // check DB whether the book is there or not -  the DB is the favourite DB
                            val book : BookEntity? = db.bookDao().getBookById(bookEntity.book_id.toString())
                            db.close()
                            return book != null
                        }
                        2 -> {

                            // insert the book entity inside the favourite database
                            db.bookDao().insertBook(bookEntity)
                            db.close()
                            return true
                        }
                        3 ->{
                            // delete a book from the favourite database
                            db.bookDao().deleteBook(bookEntity)
                            db.close()
                            return true

                        }
                    }

                    return false
                }


            }
    }
