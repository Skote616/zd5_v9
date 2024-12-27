package com.example.universities


import androidx.room.*

@Dao
interface UniversityDao {
    @Query("SELECT * FROM universities")
    fun getAllUniversities(): List<University>

    @Insert
    fun insertUniversity(university: University)

    @Delete
    fun deleteUniversity(university: University)

    @Update
    fun updateUniversity(university: University)

    @Query("SELECT university FROM universities")
    fun getAllUniversityNames(): List<String>

    @Query("SELECT * FROM universities WHERE university LIKE '%' || :searchUniversity || '%'")
    fun searchUniversityByName(searchUniversity: String): List<University>


}