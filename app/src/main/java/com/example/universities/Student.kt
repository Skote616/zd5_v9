package com.example.universities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val university: String,
    val specialty: String,
    val birthDate: String,
    val scholarship: Boolean
)