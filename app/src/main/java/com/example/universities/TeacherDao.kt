package com.example.universities

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TeacherDao {
    @Query("SELECT * FROM teachers")
    fun getAllTeachers(): List<Teacher>
    @Insert
    fun insertTeacher(teacher: Teacher)
    @Delete
    fun deleteTeacher(teacher: Teacher)
    @Update
    fun updateTeacher(teacher: Teacher)

}
//@Query("SELECT * FROM teachers WHERE specialty = :specialty")
//    fun getTeachersBySpecialty(specialty: String): List<Teacher>