package com.example.universities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast

class StudentActivity : AppCompatActivity() {
    private lateinit var university: EditText
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)
        university = findViewById(R.id.search)
    }

    fun searchUniversity(view: View) {
        if (university.text.isNotEmpty())
        {
            val intent = Intent(this, SearchUniversity::class.java)
            intent.putExtra("university", university.text.toString())
            startActivity(intent)
        }
        else
        {
            Toast.makeText(this, "Введите название университета", Toast.LENGTH_SHORT).show()
        }
    }
}