package com.example.universities

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [University::class, Student::class, Teacher::class], version = 4, exportSchema = false)
abstract class UniversityDatabase : RoomDatabase() {
    abstract fun universityDao(): UniversityDao
    abstract fun studentDao(): StudentDao
    abstract fun teacherDao(): TeacherDao


}