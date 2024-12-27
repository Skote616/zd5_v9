package com.example.universities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchStudents : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterForSearchStudent
    private lateinit var database: UniversityDatabase
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_students)
        database = Room.databaseBuilder(
            this,
            UniversityDatabase::class.java, "database6"
        )
            .fallbackToDestructiveMigration()
            .build()
        recyclerView = findViewById(R.id.list1)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AdapterForSearchStudent()
        recyclerView.adapter = adapter
        val student = intent.getStringExtra("student")
        GlobalScope.launch(Dispatchers.IO){
            val students = database.studentDao().searchStudentsByName(student.toString())
            withContext(Dispatchers.Main) {
                adapter.submitList(students)
            }
        }
    }
}