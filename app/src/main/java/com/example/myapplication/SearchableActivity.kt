package com.example.myapplication

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_searchable.*

class SearchableActivity : AppCompatActivity() {
    private val results = ArrayList<User>()
    private lateinit var adapter: Searchadapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchable)
        setSupportActionBar(toolbar2)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val userdao = AppDatabase.getDatabase(this).userDao()
        val lay = GridLayoutManager(this, 1)
        rec2.layoutManager = lay
        adapter = Searchadapter(this, results)
        rec2.adapter = adapter
        adapter.setOnItemClickListener(object : Searchadapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
            }
        })
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                for(i in userdao.chaxun(query))
                    results.add(i)
                    adapter.notifyDataSetChanged()
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}