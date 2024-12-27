package com.example.universities


import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface StudentDao {
    @Query("SELECT * FROM students")
    fun getAllStudents(): List<Student>

    @Insert
    fun insertStudent(student: Student)

    @Delete
    fun deleteStudent(student: Student)

    @Update
    fun updateTeacher(student: Student)

    @Query("SELECT * FROM students WHERE name LIKE '%' || :searchName || '%'")
    fun searchStudentsByName(searchName: String): List<Student>
}