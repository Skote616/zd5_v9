package com.example.universities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.room.Room
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AddUniversity : AppCompatActivity() {
    private lateinit var title: EditText
    private lateinit var website: EditText
    private lateinit var photo: EditText
    private lateinit var database: UniversityDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_university)
        title = findViewById(R.id.name)
        website = findViewById(R.id.web)
        photo = findViewById(R.id.urlphoto)
    }

    fun back(view: View) {
        val intent = Intent(this, MinistryActivity::class.java)
        startActivity(intent)
    }

    fun adduniv(view: View) {
        database = Room.databaseBuilder(
            applicationContext,
            UniversityDatabase::class.java, "database6"
        ).build()
        var info1 = title.text.toString()
        var info2 = website.text.toString()
        var info3 = photo.text.toString()
        if (title.text.toString().isNotEmpty() && title.text.toString() != null && website.text.toString().isNotEmpty() && website.text.toString() != null && photo.text.toString().isNotEmpty() && photo.text.toString() != null)
        {
            if (ValidUrl(info2))
            {
                val imageAvailable = runBlocking{CheckImageAvailability(info3)}
                if (!imageAvailable)
                {
                    info3 = "https://cdn-icons-png.flaticon.com/512/4054/4054617.png"
                }
                var data = University(university = info1, web = info2, photo = info3)
                GlobalScope.launch(Dispatchers.IO) {
                    database.universityDao().insertUniversity(data)
                }
                val intent = Intent(this, MinistryActivity::class.java)
                startActivity(intent)
            }
            else
            {
                Toast.makeText(this, "Введите ссылку корректно", Toast.LENGTH_SHORT).show()
            }
        }
        else
        {
            Toast.makeText(this, "Введите все необходимые данные", Toast.LENGTH_SHORT).show()
        }
    }
    fun ValidUrl(url: String): Boolean {
        val urlPattern = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$"
        return Regex(urlPattern).matches(url)
    }

    suspend fun CheckImageAvailability(imageUrl: String): Boolean {
        val client = HttpClient()
        return try {
            val response: HttpResponse = client.get(imageUrl)
            true
        } catch (e: Exception) {
            false
        } finally {
            client.close()
        }
    }
}