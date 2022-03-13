package com.example.gamecollection.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamecollection.R
import com.example.gamecollection.data.GameListItem
import com.example.gamecollection.data.LoadingStatus
import com.google.android.material.progressindicator.CircularProgressIndicator

const val RAWG_API_KEY = "e1dd3dd1ae1b47a49ae5b110b5447c6c"

class MainActivity : AppCompatActivity() {
    private val tag = "MainActivity"

    private val gameSearchViewModel: GameSearchViewModel by viewModels()
    private lateinit var gameListAdapter: GameListAdapter

    private lateinit var searchInputET: EditText
    private lateinit var searchResultListRV: RecyclerView
    private lateinit var searchErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchInputET = findViewById(R.id.et_search_input)
        searchResultListRV = findViewById(R.id.rv_search_results)
        searchErrorTV = findViewById(R.id.tv_search_error)
        loadingIndicator = findViewById(R.id.loading_indicator)

        gameListAdapter = GameListAdapter(::onGameListClick)

        searchResultListRV.layoutManager = GridLayoutManager(this,2)
        searchResultListRV.setHasFixedSize(true)

        searchResultListRV.adapter = gameListAdapter

        gameSearchViewModel.results.observe(this) { results ->
            gameListAdapter.updateRepoList(results)
        }

        gameSearchViewModel.loading.observe(this){ loading->
            when(loading){
                LoadingStatus.LOADING -> {
                    loadingIndicator.visibility = View.VISIBLE
                    searchResultListRV.visibility = View.INVISIBLE
                    searchErrorTV.visibility = View.INVISIBLE
                }
                LoadingStatus.ERROR -> {
                    loadingIndicator.visibility = View.INVISIBLE
                    searchResultListRV.visibility = View.INVISIBLE
                    searchErrorTV.visibility = View.VISIBLE
                }
                else -> {
                    loadingIndicator.visibility = View.INVISIBLE
                    searchResultListRV.visibility = View.VISIBLE
                    searchErrorTV.visibility = View.INVISIBLE
                }
            }
        }


        val searchButton: Button = findViewById(R.id.bt_search)
        searchButton.setOnClickListener {
            val searchQuery = searchInputET.text.toString()

            if (!TextUtils.isEmpty(searchQuery)) {
                // Results on search are from the user's input
                gameSearchViewModel.loadResults(RAWG_API_KEY, searchQuery, null, null)
            }
        }

        // Results on start are the most popular games in 2021
        gameSearchViewModel.loadResults(RAWG_API_KEY, null, "2021-01-01,2021-12-31", "-added")
    }
    override fun onResume() {
        Log.d(tag, "onResume()")
        super.onResume()
    }

    override fun onPause() {
        Log.d(tag, "onPause()")
        super.onPause()
    }

    private fun onGameListClick(gameListItem: GameListItem) {
        Log.d(tag, gameListItem.toString())
        val intent = Intent(this, GameDetailActivity::class.java).apply {
            putExtra(EXTRA_GAME_ID,gameListItem.id)
        }
        startActivity(intent)
    }

}