package com.example.universities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast

class TeacherActivity : AppCompatActivity() {
    private lateinit var student: EditText
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher)
        student = findViewById(R.id.search)
    }

    fun searchStudent(view: View) {
        if (student.text.isNotEmpty())
        {
            val intent = Intent(this, SearchStudents::class.java)
            intent.putExtra("student", student.text.toString())
            startActivity(intent)
        }
        else
        {
            Toast.makeText(this, "Введите ФИО студента", Toast.LENGTH_SHORT).show()
        }
    }
}