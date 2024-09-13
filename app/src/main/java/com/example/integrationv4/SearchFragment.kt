package com.example.integrationv4

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class SearchFragment : Fragment() {

    private val apiKey = "f1397c64f8122628ae99a34d1d8059be"
    private val baseImageUrl = "https://image.tmdb.org/t/p/w500"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val editTextSearch: EditText = view.findViewById(R.id.editTextSearch)
        val buttonSearch: Button = view.findViewById(R.id.buttonSearch)
        val linearLayoutSearchResults: LinearLayout = view.findViewById(R.id.linearLayoutSearchResults)

        buttonSearch.setOnClickListener {
            val query = editTextSearch.text.toString()
            if (query.isNotEmpty()) {

                linearLayoutSearchResults.removeAllViews()

                displaySearchResults(query, linearLayoutSearchResults)
            }
        }

        return view
    }

    // Funktion för att hämta och visa sökresultat
    private fun displaySearchResults(query: String, linearLayout: LinearLayout) {
        val url = URL("https://api.themoviedb.org/3/search/movie?api_key=$apiKey&query=$query")
        val connection = url.openConnection() as HttpURLConnection
        try {
            connection.requestMethod = "GET"
            connection.connect()

            val inputStream = connection.inputStream.bufferedReader().use { it.readText() }
            val jsonResponse = JSONObject(inputStream)
            val results = jsonResponse.getJSONArray("results")

            for (i in 0 until results.length()) {
                val movie = results.getJSONObject(i)
                val title = movie.getString("title")
                val releaseDate = movie.optString("release_date", "N/A") // Kan vara null
                val overview = movie.getString("overview")
                val posterPath = movie.optString("poster_path", "")

                val titleTextView = TextView(context)
                titleTextView.text = "Title: $title"
                titleTextView.textSize = 18f

                val releaseDateTextView = TextView(context)
                releaseDateTextView.text = "Release Date: $releaseDate"
                releaseDateTextView.textSize = 14f

                val overviewTextView = TextView(context)
                overviewTextView.text = "Overview: $overview"
                overviewTextView.textSize = 14f

                val imageViewPoster = ImageView(context)
                imageViewPoster.layoutParams = LinearLayout.LayoutParams(200, 300)

                if (posterPath.isNotEmpty()) {
                    Glide.with(this)
                        .load("$baseImageUrl$posterPath")
                        .into(imageViewPoster)
                }

                linearLayout.addView(imageViewPoster)
                linearLayout.addView(titleTextView)
                linearLayout.addView(releaseDateTextView)
                linearLayout.addView(overviewTextView)

                val spacer = TextView(context)
                spacer.text = "\n\n"
                linearLayout.addView(spacer)
            }

        } catch (e: Exception) {
            Log.e("SearchFragment", "Error searching for movies", e)
        } finally {
            connection.disconnect()
        }
    }
}