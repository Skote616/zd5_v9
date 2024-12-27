package com.example.universities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teachers")
data class Teacher(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val university: String,
    val specialty: String,
    val salary: Double,
    val teachingHours: Int
)