package com.example.integrationv4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val buttonTopRated: Button = view.findViewById(R.id.buttonTopRated)
        val buttonTrending: Button = view.findViewById(R.id.buttonTrending)
        val buttonSearch: Button = view.findViewById(R.id.buttonSearch)

        buttonTopRated.setOnClickListener {
            (activity as MainActivity).loadFragment(TopRatedMoviesFragment())
        }

        buttonTrending.setOnClickListener {
            (activity as MainActivity).loadFragment(TrendingMoviesFragment())
        }

        buttonSearch.setOnClickListener {
            (activity as MainActivity).loadFragment(SearchFragment())
        }

        return view
    }
}