package com.example.kotlinbookapp.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.kotlinbookapp.R
import com.example.kotlinbookapp.adapter.RecyclerDashboardAdapter
import com.example.kotlinbookapp.model.Book
import com.example.kotlinbookapp.util.ConnectionManager
import org.json.JSONException

class DashboardFragment : Fragment() {

    // create a reference to the recyclerview
    lateinit var recyclerview : RecyclerView
   lateinit var recyclerDashboardAdapter : RecyclerDashboardAdapter
   lateinit var btn_check_internet : Button

    // create a reference to the LayoutManager
    lateinit var layoutManager : RecyclerView.LayoutManager

    lateinit var progressLayout : RelativeLayout
    lateinit var progressbar : ProgressBar

    val bookList = arrayListOf<Book>()




    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        // initialise progress bar container layout
        progressLayout = view.findViewById(R.id.progressLayout)


        // initialise progress bar reference

        progressbar = view.findViewById(R.id.progressbar)

        // make visible the progress bar container layout

        progressLayout.visibility = View.VISIBLE




        // initialise the recyclerview
        recyclerview = view.findViewById(R.id.recyclerView)
        // initialise the layout manager
        layoutManager = LinearLayoutManager(activity)





        val queue = Volley.newRequestQueue(activity as Context)
        val uri = "http://13.235.250.119/v1/book/fetch_books/"

        if(ConnectionManager().checkConnectivity(activity as Context)) {


            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, uri, null, Response.Listener {

                    try {

                        // hide the progress bar layout
                        progressLayout.visibility = View.GONE

                        val success = it.getBoolean("success")
                        if (success) {
                            val data = it.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val bookJsonObject = data.getJSONObject(i)
                                // create a book instance using the data from the bookJsonObject
                                val bookObject = Book(
                                        bookJsonObject.getString("book_id"),
                                        bookJsonObject.getString("name"),
                                        bookJsonObject.getString("author"),
                                        bookJsonObject.getString("price"),
                                        bookJsonObject.getString("rating"),
                                        bookJsonObject.getString("image")

                                )
                                bookList.add(bookObject)
                            }
                            // instantiate the dashboard adapter
                            recyclerDashboardAdapter =
                                    RecyclerDashboardAdapter(activity as Context, bookList)
                            // set the adapter and the linear layout to the recycler view object
                            recyclerview.adapter = recyclerDashboardAdapter
                            recyclerview.layoutManager = layoutManager


                        } else {
                            if(activity != null) {
                            Toast.makeText(

                                    activity as Context,
                                    "Some Error Has Occured in Else",
                                    Toast.LENGTH_LONG
                            ).show()
                            }
                        }
                    } catch (e: JSONException) {
                        if(activity != null) {
                            Toast.makeText(activity as Context, e.message, Toast.LENGTH_SHORT).show()
                        }
                    }

                }, Response.ErrorListener {
                if(activity != null) {
                    Toast.makeText(activity as Context,"Volley encountered some error!!!",Toast.LENGTH_SHORT).show()
                }

            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers =  HashMap<String,String>()
                    headers["Content-type"] =  "application/json"
                    headers["token"] = "9bf534118365f1"
                    return headers
                }
            }

            queue.add(jsonObjectRequest)
        } else {

            // Internet is not available
            val dialog = AlertDialog.Builder(activity as Context)
            if (activity != null) {
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection is not  Found")
                dialog.setPositiveButton("Open Settings") { text, listener ->

                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    activity?.finish()

                }
                dialog.setNegativeButton("Exit") { text, listener ->

                    ActivityCompat.finishAffinity(activity as Activity)
                }
                dialog.create()
                dialog.show()
            }

        }
        return view
    }


}