package com.example.universities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class AddStudent : AppCompatActivity() {
    private lateinit var name: EditText
    private lateinit var univer: Spinner
    private lateinit var speciality: EditText
    private lateinit var birthday: DatePicker
    private lateinit var budget: CheckBox
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)
        name = findViewById(R.id.name)
        univer = findViewById(R.id.spinner)
        speciality = findViewById(R.id.speciality)
        birthday = findViewById(R.id.date)
        budget = findViewById(R.id.budget)
        val calendar = Calendar.getInstance()
        val calendar1 = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        calendar.set(currentYear - 15, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        calendar1.set(currentYear - 35, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        val maxDate = calendar.timeInMillis
        val minDate = calendar1.timeInMillis
        runOnUiThread {
            birthday.maxDate = maxDate
            birthday.minDate = minDate
        }
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
    fun addstudent(view: View) {
        database = Room.databaseBuilder(
            applicationContext,
            UniversityDatabase::class.java, "database6"
        ).build()
        var info1 = name.text.toString()
        var info2 = univer.selectedItem.toString()
        var info3 = speciality.text.toString()
        var year = birthday.year
        var month = birthday.month+1
        var day = birthday.dayOfMonth
        var info4 = "$year-$month-$day"
        var info5 = budget.isChecked
        if (info1.isNotEmpty() && info1 != null && info3.isNotEmpty() && info3 != null)
        {
            var data = Student(name = info1, university = info2, specialty = info3, birthDate = info4, scholarship = info5)
            GlobalScope.launch(Dispatchers.IO) {
                database.studentDao().insertStudent(data)
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