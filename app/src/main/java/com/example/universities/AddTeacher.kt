package com.example.universities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddTeacher : AppCompatActivity() {
    private lateinit var name: EditText
    private lateinit var univer: Spinner
    private lateinit var speciality: EditText
    private lateinit var salary: EditText
    private lateinit var hours: EditText
    private lateinit var database: UniversityDatabase
    private val universityDao: UniversityDao by lazy {

        val database = Room.databaseBuilder(
            this,
            UniversityDatabase::class.java,
            "database6"
        ).allowMainThreadQueries()
            .build()
        database.universityDao()
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_teacher)
        name = findViewById(R.id.name)
        univer = findViewById(R.id.spinner)
        speciality = findViewById(R.id.speciality)
        salary = findViewById(R.id.salary)
        hours = findViewById(R.id.hours)
        val universityNames = universityDao.getAllUniversityNames()

        val universityAdapter = ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_item, universityNames
        )
        universityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        univer.adapter = universityAdapter
    }

    fun back(view: View) {
        val intent = Intent(this, MinistryActivity::class.java)
        startActivity(intent)
    }
    fun addteacher(view: View) {
        database = Room.databaseBuilder(
            applicationContext,
            UniversityDatabase::class.java, "database6"
        ).build()
        var info1 = name.text.toString()
        var info2 = speciality.text.toString()
        var info3 = salary.text.toString()
        var info4 = hours.text.toString()
        var info5 = univer.selectedItem.toString()
        if (info1.isNotEmpty() && info1 != null && info2.isNotEmpty() && info2 != null && info3.isNotEmpty() && info3 != null && info4.isNotEmpty() && info4 != null)
        {
            if (info4.toInt()>1440)
                info3 = (info3.toDouble() + ((info4.toDouble()-1440)*300)).toString()
            var data = Teacher(name = info1, university = info5, specialty = info2, salary = info3.toDouble(), teachingHours = info4.toInt())
            GlobalScope.launch(Dispatchers.IO) {
                database.teacherDao().insertTeacher(data)
            }
            val intent = Intent(this, MinistryActivity::class.java)
            startActivity(intent)
        }
        else
        {
            Toast.makeText(this, "Введите все необходимые данные", Toast.LENGTH_SHORT).show()
        }
    }
}