package com.example.universities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "universities")
data class University(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val photo: String,
    val university: String,
    val web: String
)