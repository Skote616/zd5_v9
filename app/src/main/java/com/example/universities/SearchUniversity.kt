package com.example.universities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchUniversity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterForSearchUniversity
    private lateinit var database: UniversityDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_university)
        database = Room.databaseBuilder(
            this,
            UniversityDatabase::class.java, "database6"
        )
            .fallbackToDestructiveMigration()
            .build()
        recyclerView = findViewById(R.id.list1)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AdapterForSearchUniversity()
        recyclerView.adapter = adapter
        val university = intent.getStringExtra("university")
        GlobalScope.launch(Dispatchers.IO){
            val universities = database.universityDao().searchUniversityByName(university.toString())
            withContext(Dispatchers.Main) {
                adapter.submitList(universities)
            }
        }
    }
}