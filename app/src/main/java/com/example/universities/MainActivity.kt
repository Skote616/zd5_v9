package com.example.universities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var login: EditText
    lateinit var password: EditText
    lateinit var mode: Spinner
    lateinit var shar: SharedPreferences
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        login = findViewById(R.id.log)
        password = findViewById(R.id.pass)
        mode = findViewById(R.id.menu)
        shar = getSharedPreferences("data", MODE_PRIVATE)
        var correctlogin = shar.getString("login", null)
        var correctpassword = shar.getString("password", null)
        if (correctlogin != null && correctpassword != null)
        {
            login.setText(correctlogin)
            password.setText(correctpassword)
        }
    }

    fun vhod(view: View) {
        var log = login.text.toString()
        var pass = password.text.toString()
        var menu = mode.selectedItem.toString()
        shar = getSharedPreferences("data", MODE_PRIVATE)
        if (log.isEmpty() || pass.isEmpty())
        {
            Toast.makeText(this, "Введите логин и пароль", Toast.LENGTH_SHORT).show()
        }
        else if (pass.length < 6)
        {
            Toast.makeText(this, "Для ввода пароля необходимо минимум 6 символов", Toast.LENGTH_SHORT).show()
        }
        else
        {
            shar = getSharedPreferences("data", MODE_PRIVATE)
            var correctlogin = shar.getString("login", null)
            var correctpassword = shar.getString("password", null)
            var selectedMode = shar.getString("mode", null)
            if (correctlogin != null && correctpassword != null)
            {
                if (correctlogin == log && correctpassword == pass && selectedMode == menu)
                {
                    when (selectedMode){
                        "Министерство" ->
                        {
                            var intent = Intent(this, MinistryActivity::class.java)
                            startActivity(intent)
                        }
                        "Преподаватель" ->
                        {
                            var intent = Intent(this, TeacherActivity::class.java)
                            startActivity(intent)
                        }
                        "Студент" ->
                        {
                            var intent = Intent(this, StudentActivity::class.java)
                            startActivity(intent)
                        }
                    }

                }
                else
                {
                    Toast.makeText(this, "Неверный логин или пароль или вашему аккаунту недоступен данный режим", Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                var edit = shar.edit()
                edit.putString("login", log)
                edit.putString("password", pass)
                edit.apply()
            }

        }
    }

    fun registr(view: View) {
        var log = login.text.toString()
        var pass = password.text.toString()
        var menu = mode.selectedItem.toString()
        if (log.isEmpty() || pass.isEmpty())
        {
            Toast.makeText(this, "Введите логин и пароль", Toast.LENGTH_SHORT).show()
        }
        else if (pass.length < 6)
        {
            Toast.makeText(this, "Для ввода пароля необходимо минимум 6 символов", Toast.LENGTH_SHORT).show()
        }
        else
        {
            var edit = shar.edit()
            edit.putString("login", log)
            edit.putString("password", pass)
            edit.putString("mode", menu)
            edit.apply()
            Toast.makeText(this, "Регистрация прошла успешно", Toast.LENGTH_SHORT).show()
        }

    }
}