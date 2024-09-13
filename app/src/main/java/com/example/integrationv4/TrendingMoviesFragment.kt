package com.example.integrationv4

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class TrendingMoviesFragment : Fragment() {

    private val apiKey = "f1397c64f8122628ae99a34d1d8059be"
    private val baseImageUrl = "https://image.tmdb.org/t/p/w500"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_trending_movies, container, false)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val linearLayoutMovies: LinearLayout = view.findViewById(R.id.linearLayoutMovies)

        displayTrendingMovies(linearLayoutMovies)

        return view
    }

    private fun displayTrendingMovies(linearLayout: LinearLayout) {
        val url = URL("https://api.themoviedb.org/3/trending/movie/week?api_key=$apiKey")
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
                val releaseDate = movie.optString("release_date", "N/A") // release_date kan vara null
                val overview = movie.getString("overview")
                val posterPath = movie.getString("poster_path")

                val imageViewPoster = ImageView(context)
                imageViewPoster.layoutParams = LinearLayout.LayoutParams(200, 300)

                Glide.with(this)
                    .load("$baseImageUrl$posterPath")
                    .into(imageViewPoster)

                val titleTextView = TextView(context)
                titleTextView.text = "Title: $title"
                titleTextView.textSize = 18f

                val releaseDateTextView = TextView(context)
                releaseDateTextView.text = "Release Date: $releaseDate"
                releaseDateTextView.textSize = 14f

                val overviewTextView = TextView(context)
                overviewTextView.text = "Overview: $overview"
                overviewTextView.textSize = 14f

                linearLayout.addView(imageViewPoster)
                linearLayout.addView(titleTextView)
                linearLayout.addView(releaseDateTextView)
                linearLayout.addView(overviewTextView)

                val spacer = TextView(context)
                spacer.text = "\n\n"
                linearLayout.addView(spacer)
            }

        } catch (e: Exception) {
            Log.e("TrendingMoviesFragment", "Error fetching trending movies", e)
        } finally {
            connection.disconnect()
        }
    }
}