package com.example.videoassignment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

val supabase = createSupabaseClient(
    supabaseUrl = "https://ifrulcrawgjkklfpbttq.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImlmcnVsY3Jhd2dqa2tsZnBidHRxIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTQ5OTI1MDcsImV4cCI6MjAzMDU2ODUwN30.q32yTOE1dO_5XtpV5tiXBhYilfY1fJ--_3UkHuptSwo"
) {
    install(Postgrest)
}

class MainActivity : AppCompatActivity() {
    val adapter=video_reccler_view_adapter(this@MainActivity)
    private val videoList: MutableList<video_data_class> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        recyclerView.adapter=adapter
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val results = supabase.from("videoTable").select().decodeList<video_data_class>()
                if (results.isNotEmpty()) {
                    // Switch to the main thread to update UI components
                    GlobalScope.launch(Dispatchers.Main) {
                        adapter.setData(results.toMutableList())
                        videoList.addAll(results.toMutableList())
                    }
                }
            } catch (e: Exception) {
                // Log any exceptions that occur
                Log.e("MainActivity", "Error fetching or setting data.", e)
            }
        }
        val searchView = findViewById<SearchView>(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.setData(filter(newText,videoList))
                return false
            }

        })

    }
    private fun filter(text: String,VideoList:List<video_data_class>):List<video_data_class> {
        // creating a new array list to filter our data.
        val filteredlist: ArrayList<video_data_class> = ArrayList()

        // running a for loop to compare elements.
        for (item in VideoList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.Title.toLowerCase().contains(text.toLowerCase())||item.Channel_Name.toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            return filteredlist
        }
        return VideoList.toMutableList()
    }
}